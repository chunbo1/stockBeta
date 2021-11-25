package com.stockbeta;
import com.util.AppStartupRunner;
import com.util.CsvParser;
import com.util.CsvParserImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import com.object.factory.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Configuration
@EnableAutoConfiguration
@EnableConfigurationProperties({StockBetaSettings.class})
public class StockBetaDiConfig {
    //private final ObjectFactory<StockBetaProvider> stockBetaProviderFactory;
    private final StockBetaSettings stockBetaSettings;
    private final CsvParser<StockPriceRecord> csvParser;
    private final BetaCalculatorService betaCalculatorService;
    private final FileMonitor fileMonitor;
    private final KafkaMonitor kafkaMonitor;
    private StockBetaProvider stockBetaProvider;

    public StockBetaDiConfig(StockBetaSettings stockBetaSettings,
                             //@Lazy ObjectFactory<StockBetaProvider> stockBetaProviderFactory,
                             @Lazy StockBetaProvider stockBetaProvider,
                             @Lazy CsvParser<StockPriceRecord> csvParser,
                             @Lazy BetaCalculatorService betaCalculator,
                             @Lazy FileMonitor fileMonitor,
                             @Lazy KafkaMonitor kafkaMonitor)
    {
        HashMap<String, String> newBankHolidayMap = new HashMap<>();
        Iterator<Map.Entry<String, String>> it = stockBetaSettings.getBankHolidays().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            newBankHolidayMap.put(entry.getKey().replace("-", "/"), entry.getValue());
        }
        stockBetaSettings.setBankHolidays(newBankHolidayMap);

        this.stockBetaSettings = stockBetaSettings;
        //this.stockBetaProviderFactory = stockBetaProviderFactory;
        this.stockBetaProvider = stockBetaProvider;
        this.csvParser = csvParser;
        this.betaCalculatorService = betaCalculator;
        this.fileMonitor = fileMonitor;
        this.kafkaMonitor = kafkaMonitor;

    }

    //1: dep[0] which is ConfigureFile constructor
    @Bean
    public <T> ObjectFactory<T> objectFactory(ApplicationContext applicationContext) {
        return new ObjectFactoryImpl<>(applicationContext);
    }

    //2: dep[] note we use it as Singleton so StockBetaController can share the same instance of this bean, so don't use @Scope("prototype")
    @Bean
    public StockBetaProvider stockBetaProvider() {
        return new StockBetaProviderImpl(betaCalculatorService, fileMonitor, kafkaMonitor);
    }


    //3: dep[1, 2]
    //SpringBootDemoApplication.main {SpringApplication.run => start creating beans in DiConfig file }
    //in order to use ApplicationRunner or CommandLineRunner interfaces, one needs to create a Spring bean and implement
    // either ApplicationRunner or CommandLineRunner interfaces, both perform similarly.
    // Once complete, your Spring application will detect your bean.
    @Bean
    public ApplicationRunner stockBetaProviderRunner() {
        //ApplicationRunner run() will get execute, just after applicationcontext is created and before spring boot application startup.
        log.info("STEP2: instantiate ApplicationRunner");
        return new AppStartupRunner("stockBetaProviderRunner", new Consumer<ApplicationArguments>() {
            @Override
            public void accept(ApplicationArguments applicationArguments) {
                log.info("STEP4: Consumer.accept() get invoked by AppStartupRunner.run()");
                //final StockBetaProvider stockBetaprovider = stockBetaProviderFactory.create("stockBetaProvider");this will create another instance, but what we need is a singleton
                stockBetaProvider.run();
            }
        });
    }

    @Bean
    @Scope("prototype")
    public CsvParser csvParser() { return new CsvParserImpl<StockPriceRecord>(StockPriceRecord.class); }

    @Bean
    @Scope("prototype")
    public BetaCalculatorService betaCalculator() {
        return new BetaCalculatorServiceImpl();
    }

    @Bean
    public FileMonitor fileMonitor() { return new FileMonitorImpl(csvParser, stockBetaSettings, betaCalculatorService); }

    @Bean
    public KafkaMonitor kafkaMonitor() { return new KafkaMonitorImpl(); }


}
