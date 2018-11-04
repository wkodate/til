package com.wkodate.springboot.domain.service.reservation;

/**
 * Created by wkodate on 2018/11/04.
 */
public class AlreadyReservedException extends RuntimeException {
    public AlreadyReservedException(String message) {
        super(message);
    }
}
