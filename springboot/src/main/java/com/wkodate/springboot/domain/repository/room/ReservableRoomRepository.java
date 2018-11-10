package com.wkodate.springboot.domain.repository.room;

import com.wkodate.springboot.domain.model.ReservableRoom;
import com.wkodate.springboot.domain.model.ReservableRoomId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;

/**
 * Created by wkodate on 2018/10/27.
 */
public interface ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ReservableRoom findOneForUpdateByReservableRoomId(ReservableRoomId reservableRoomId);

    List<ReservableRoom> findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(LocalDate reservedDate);
}
