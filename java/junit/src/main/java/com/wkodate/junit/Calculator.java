package com.wkodate.junit;

/**
 * Created by wkodate on 2018/11/19.
 */
public class Calculator {

    public int divide(int x, int y) {
        if (y == 0) throw new IllegalArgumentException("y is null");
        return x / y;
    }

}
