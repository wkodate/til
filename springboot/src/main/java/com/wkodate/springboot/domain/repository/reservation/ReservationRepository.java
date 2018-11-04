package com.wkodate.springboot.domain.repository.reservation;

import com.wkodate.springboot.domain.model.ReservableRoomId;
import com.wkodate.springboot.domain.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by wkodate on 2018/11/04.
 */
public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
            ReservableRoomId reservableRoomId);
}
