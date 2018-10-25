package com.wkodate.springboot.domain.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by wkodate on 2018/10/25.
 */
@Entity
public class ReservableRoom implements Serializable {

    @EmbeddedId
    private ReservableRoomId reservableRoomId;

    @ManyToOne
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    @MapsId("roomId")
    private MeetingRoom meetingRoom;

    public ReservableRoom(ReservableRoomId reservableRoomId) {
        this.reservableRoomId = reservableRoomId;
    }

    public ReservableRoom() {
    }

}
