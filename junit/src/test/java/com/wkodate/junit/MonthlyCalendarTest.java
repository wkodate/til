package com.wkodate.junit;

import org.junit.Test;

import java.util.Calendar;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by wkodate on 2018/11/26.
 */
public class MonthlyCalendarTest {

    @Test
    public void testGetRemainingDays_2012_1_31() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 0, 31);
        assertThat(new MonthlyCalendar(cal).getRemainingDays(), is(0));
    }
    @Test
    public void testGetRemainingDays_2012_1_30() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 0, 30);
        assertThat(new MonthlyCalendar(cal).getRemainingDays(), is(1));
    }
    @Test
    public void testGetRemainingDays_2012_2_1() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(2012, 1, 1);
        assertThat(new MonthlyCalendar(cal).getRemainingDays(), is(28));
    }
}