package com.wkodate.junit;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by wkodate on 2018/11/19.
 */
public class StringUtilsTest {

    @Test
    public void testToSnakeCase() throws Exception {
        assertThat(StringUtils.toSnakeCase("aaa"), is("aaa"));
        assertThat(StringUtils.toSnakeCase("HelloWorld"), is("hello_world"));
        assertThat(StringUtils.toSnakeCase("practiceJunit"), is("practice_junit"));
    }
}