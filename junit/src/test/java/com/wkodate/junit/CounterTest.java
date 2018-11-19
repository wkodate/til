package com.wkodate.junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * 副作用を持つメソッドのテスト.
 */
@RunWith(Enclosed.class)
public class CounterTest {

    public static class incrementOnce {
        Counter counter;

        @Before
        public void setUp() throws Exception {
            counter = new Counter();
        }

        @Test
        public void increment() throws Exception {
            assertThat(counter.increment(), is(1));
        }
    }

    public static class incrementTwice {
        Counter counter;

        @Before
        public void setUp() throws Exception {
            counter = new Counter();
            counter.increment();
        }

        @Test
        public void increment() throws Exception {
            assertThat(counter.increment(), is(2));
        }
    }

    public static class incrementFiftyTimes {
        Counter counter;

        @Before
        public void setUp() throws Exception {
            counter = new Counter();
            for (int i = 0; i < 50; i++) {
                counter.increment();
            }
        }

        @Test
        public void increment() throws Exception {
            assertThat(counter.increment(), is(51));
        }
    }

}