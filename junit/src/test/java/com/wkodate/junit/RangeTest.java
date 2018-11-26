package com.wkodate.junit;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * 境界値のテスト.構造化、パラメータ化テストを使用.
 */
@RunWith(Enclosed.class)
public class RangeTest {

    public static class RangeTest1 {

        Range range;

        @Before
        public void setUp() {
            range = new Range(0.0, 10.5);
        }

        @Test
        public void testContains() throws Exception {
            assertThat(range.contains(-0.1), is(false));
            assertThat(range.contains(0.0), is(true));
            assertThat(range.contains(10.5), is(true));
            assertThat(range.contains(10.6), is(false));
        }
    }

    public static class RangeTest2 {

        Range range;

        @Before
        public void setUp() {
            range = new Range(0.0, 10.5);
        }

        @Test
        public void sameMinAsMax() throws Exception {
            Range range = new Range(-5.1, 5.1);
            assertThat(range.contains(-5.2), is(false));
            assertThat(range.contains(-5.1), is(true));
            assertThat(range.contains(5.1), is(true));
            assertThat(range.contains(5.2), is(false));
        }

    }
}