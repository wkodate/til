package com.wkodate.junit;

import java.util.Calendar;

/**
 * Created by wkodate on 2018/11/26.
 */
public class MonthlyCalendar {

    private final Calendar cal;

    public MonthlyCalendar() {
        this(Calendar.getInstance());
    }

    MonthlyCalendar(Calendar cal) {
        this.cal = cal;
    }

    public int getRemainingDays() {
        return cal.getActualMaximum(Calendar.DATE) - cal.get(Calendar.DATE);
    }

}
