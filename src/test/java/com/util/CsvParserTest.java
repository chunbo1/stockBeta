package com.util;
import com.stockbeta.StockPriceRecord;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CsvParserTest {
    private CsvParser<StockPriceRecord> csvParser;
    @Before
    public void before() {
        csvParser = new CsvParserImpl<StockPriceRecord>(StockPriceRecord.class);
    }

    @Test
    public void parseTest() {
        List<StockPriceRecord> list = csvParser.parse("C:\\DEV\\StockBeta\\QQQ.csv");
        assertThat(list.size()).isEqualTo(5713);
    }
}
