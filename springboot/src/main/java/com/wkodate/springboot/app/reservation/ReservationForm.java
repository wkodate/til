package com.wkodate.springboot.app.reservation;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

/**
 * Created by wkodate on 2018/11/06.
 */
@Getter
@Setter
public class ReservationForm implements Serializable {
    @NotNull(message = "必須です")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @NotNull(message = "必須です")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime endTime;
}
