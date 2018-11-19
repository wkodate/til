package com.wkodate.junit;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * 状態を持たないメソッドのテスト.
 */
public class StringUtilsTest {

    @Test
    public void testToSnakeCase() throws Exception {
        assertThat(StringUtils.toSnakeCase("aaa"), is("aaa"));
        assertThat(StringUtils.toSnakeCase("HelloWorld"), is("hello_world"));
        assertThat(StringUtils.toSnakeCase("practiceJunit"), is("practice_junit"));
    }
}