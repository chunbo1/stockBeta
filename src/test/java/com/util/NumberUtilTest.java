package com.util;
import org.junit.Test;
import static com.util.NumberUtil.isNumeric;
import static com.util.NumberUtil.round;
import static org.assertj.core.api.Assertions.assertThat;

public class NumberUtilTest {
    @Test
    public void isNumericTest() {
        assertThat(isNumeric("22")).isTrue();
        assertThat(isNumeric("5.00")).isTrue();
        assertThat(isNumeric(null)).isFalse();
    }
    @Test
    public void roundTest() {
        assertThat(round(0.123456,4)).isEqualTo(0.1235);
        assertThat(round(0.123456,2)).isEqualTo(0.12);
    }
}
