package com.wkodate.junit;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * リストのアサーション.
 */
public class FuzzBuzzTest {

    @Test
    public void testCreateFizzBuzzList() throws Exception {
        List<String> actual = FuzzBuzz.createFizzBuzzList(16);
        assertThat(actual.get(0), is("1"));
        assertThat(actual.get(1), is("2"));
        assertThat(actual.get(2), is("Fizz"));
        assertThat(actual.get(3), is("4"));
        assertThat(actual.get(4), is("Buzz"));
        assertThat(actual.get(5), is("Fizz"));
        assertThat(actual.get(6), is("7"));
        assertThat(actual.get(7), is("8"));
        assertThat(actual.get(8), is("Fizz"));
        assertThat(actual.get(9), is("Buzz"));
        assertThat(actual.get(10), is("11"));
        assertThat(actual.get(11), is("Fizz"));
        assertThat(actual.get(12), is("13"));
        assertThat(actual.get(13), is("14"));
        assertThat(actual.get(14), is("FizzBuzz"));
        assertThat(actual.get(15), is("16"));
    }

}