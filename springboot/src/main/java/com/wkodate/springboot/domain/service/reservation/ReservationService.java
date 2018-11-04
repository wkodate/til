package com.wkodate.springboot.domain.service.reservation;

import com.wkodate.springboot.domain.model.ReservableRoom;
import com.wkodate.springboot.domain.model.ReservableRoomId;
import com.wkodate.springboot.domain.model.Reservation;
import com.wkodate.springboot.domain.repository.reservation.ReservationRepository;
import com.wkodate.springboot.domain.repository.room.ReservableRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wkodate on 2018/11/04.
 */
@Service
@Transactional
public class ReservationService {
    @Autowired
    ReservationRepository reservationRepository;
    @Autowired
    ReservableRoomRepository reservableRoomRepository;

    public Reservation reserve(Reservation reservation) {
        ReservableRoomId reservableRoomId = reservation.getReservableRoom().getReservableRoomId();
        ReservableRoom reservableRoom = reservableRoomRepository.getOne(reservableRoomId);
        if (reservableRoom == null) {
            throw new UnavailableReservationException("入力の日付・部屋の組み合わせは予約できません。");
        }
        boolean overlap = reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(reservableRoomId)
                .stream()
                .anyMatch(x -> x.overlap(reservation));
        if (overlap) {
            throw new AlreadyReservedException("入力の時間帯はすでに予約済みです。");

        }
        reservationRepository.save(reservation);
        return reservation;
    }

    public List<Reservation> findReservations(ReservableRoomId reservableRoomId) {
        return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
                reservableRoomId);
    }
}
