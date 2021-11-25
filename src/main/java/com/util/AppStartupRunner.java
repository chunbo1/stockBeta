package com.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.util.Arrays;
import java.util.function.Consumer;

@Slf4j
public class AppStartupRunner implements ApplicationRunner {
    private final String name;
    private final Consumer<ApplicationArguments> runner;

    public AppStartupRunner(String name, Consumer<ApplicationArguments> runner) {
        this.name = name;
        this.runner = runner;
    }

    //STEP3
    //ApplicationRunner run() will get execute, just after applicationcontext is created and before spring boot application startup.
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AppStartupRunner {} started with arguments : {}. ", name, Arrays.toString(args.getSourceArgs()));
        log.info("STEP3: AppStartupRunner.run() get execute, just after applicationcontext is created and before spring boot application startup...");
        //the following accept() is defined in ArenaServiceDiConfig
        runner.accept(args);
    }
}
