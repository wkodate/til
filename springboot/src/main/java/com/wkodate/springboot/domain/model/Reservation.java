package com.wkodate.springboot.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Created by wkodate on 2018/10/25.
 */
@Entity
@Getter
@Setter
public class Reservation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    private LocalTime startTime;

    private LocalTime endTime;

    @ManyToOne
    @JoinColumns({@JoinColumn(name = "reserved_date"),
            @JoinColumn(name = "room_id")})
    private ReservableRoom reservableRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * 予約時刻の重複のチェック.
     *
     * @param target
     * @return
     */
    public boolean overlap(Reservation target) {
        if (!Objects.equals(reservableRoom.getReservableRoomId(), target.reservableRoom.getReservableRoomId())) {
            return false;
        }
        return startTime.equals(target.startTime) && endTime.equals(target.endTime)
                || target.endTime.isAfter(startTime) && endTime.isAfter(target.startTime);
    }

}
