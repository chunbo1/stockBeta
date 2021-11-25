package com.util;

import java.util.List;

public interface CsvParser<T> {
    List<T> parse(String path);
}
