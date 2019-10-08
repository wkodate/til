package com.wkodate.junit;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 例外を創出するメソッドのテスト.
 */
public class CalculatorTest {

    @Test(expected = IllegalArgumentException.class)
    public void testDivide() throws Exception {
        new Calculator().divide(1, 0);
    }
}