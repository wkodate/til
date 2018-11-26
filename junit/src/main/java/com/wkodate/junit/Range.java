package com.wkodate.junit;

/**
 * Created by wkodate on 2018/11/26.
 */
public class Range {

    public final double min;
    public final double max;

    public Range(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public boolean contains(double value) {
        return min <= value && value <= max;
    }

}
