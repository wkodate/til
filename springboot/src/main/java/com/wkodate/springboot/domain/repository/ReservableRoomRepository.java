package com.wkodate.springboot.domain.repository;

import com.wkodate.springboot.domain.model.ReservableRoom;
import com.wkodate.springboot.domain.model.ReservableRoomId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wkodate on 2018/10/27.
 */
public interface ReservableRoomRepository extends JpaRepository<ReservableRoom, ReservableRoomId> {
    List<ReservableRoom> findByReseervableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(LocalDate reservedDate);
}
