package com.wkodate.springboot.app.reservation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

/**
 * Created by wkodate on 2018/11/10.
 */
public class ThirtyMinutesUnitValidator implements ConstraintValidator<ThirtyMinutesUnit, LocalTime> {
    @Override
    public void initialize(ThirtyMinutesUnit constraintAnnotation) {
    }

    @Override
    public boolean isValid(LocalTime localTime, ConstraintValidatorContext constraintValidatorContext) {
        if (localTime == null) {
            return true;
        }
        return localTime.getMinute() % 30 == 0;
    }
}
