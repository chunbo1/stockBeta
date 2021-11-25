package com.util;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.SneakyThrows;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvParserImpl<T> implements CsvParser {
    private Class<T> type;
    public CsvParserImpl (Class<T> type) {
        this.type = type;
    }
    @SneakyThrows
    @Override
    public List<T> parse(String fileName) {

        try {
            List<T> listOfPrice = new CsvToBeanBuilder(new FileReader(fileName))
                    .withType(type)
                    .build()
                    .parse();
            //listOfPrice.forEach(System.out::println);
            return listOfPrice;
        } catch (IOException e) {
            throw e;
        }
    }
}
