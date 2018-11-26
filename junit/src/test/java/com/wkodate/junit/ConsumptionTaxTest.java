package com.wkodate.junit;

import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by wkodate on 2018/11/26.
 */
@RunWith(Theories.class)
public class ConsumptionTaxTest {

    @DataPoints
    public static Fixture[] PARAMS = {
            new Fixture(5,100,105),
            new Fixture(5,3000,3150),
            new Fixture(10,50,55),
            new Fixture(5,50,52),
            new Fixture(3,50,51)
    };

    @Theory
    public void testApply(Fixture f) throws Exception {
        ConsumptionTax ct = new ConsumptionTax(f.rate);
        assertThat(ct.apply(f.price), is(f.expected));
    }

    static class Fixture {
        int rate;
        int price;
        int expected;

        public Fixture(int rate, int price, int expected) {
            this.rate = rate;
            this.price = price;
            this.expected = expected;
        }
    }

}