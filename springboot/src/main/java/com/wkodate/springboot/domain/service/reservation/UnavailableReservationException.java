package com.wkodate.springboot.domain.service.reservation;

/**
 * Created by wkodate on 2018/11/04.
 */
public class UnavailableReservationException extends RuntimeException{
    public UnavailableReservationException(String message) {
        super(message);
    }
}
