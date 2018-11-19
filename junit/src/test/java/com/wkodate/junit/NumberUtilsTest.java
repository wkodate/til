package com.wkodate.junit;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * 同値クラスに対するテスト.
 */
public class NumberUtilsTest {

    @Test
    public void testIsEven() throws Exception {
        assertThat(NumberUtils.isEven(10), is(true));
        assertThat(NumberUtils.isEven(7), is(false));
    }

}