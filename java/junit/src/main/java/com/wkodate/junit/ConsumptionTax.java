package com.wkodate.junit;

/**
 * Created by wkodate on 2018/11/26.
 */
public class ConsumptionTax {

    private final int rate;

    public ConsumptionTax(int rate) {
        this.rate = rate;
    }

    public int apply(int price) {
        return price + (price * this.rate / 100);
    }

}
