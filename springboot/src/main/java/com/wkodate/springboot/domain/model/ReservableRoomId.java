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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReservableRoomId that = (ReservableRoomId) o;

        if (roomId != null ? !roomId.equals(that.roomId) : that.roomId != null) return false;
        return !(reservedDate != null ? !reservedDate.equals(that.reservedDate) : that.reservedDate != null);

    }

    @Override
    public int hashCode() {
        int result = roomId != null ? roomId.hashCode() : 0;
        result = 31 * result + (reservedDate != null ? reservedDate.hashCode() : 0);
        return result;
    }
}
