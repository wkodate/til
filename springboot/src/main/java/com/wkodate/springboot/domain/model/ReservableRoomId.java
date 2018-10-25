package com.wkodate.springboot.domain.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * roomIdとreservedDateフィールドによる複合クラス.
 */
@Embeddable
public class ReservableRoomId implements Serializable {

    private Integer roomId;

    private LocalDate reservedDate;

    public ReservableRoomId(Integer roomId, LocalDate reservedDate) {
        this.roomId = roomId;
        this.reservedDate = reservedDate;
    }

    public ReservableRoomId() {
    }

}
