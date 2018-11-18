package com.wkodate.springboot.domain.service.reservation;

import com.wkodate.springboot.domain.model.*;
import com.wkodate.springboot.domain.repository.reservation.ReservationRepository;
import com.wkodate.springboot.domain.repository.room.ReservableRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

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

    /**
     * 予約処理.
     *
     * @param reservation
     * @return
     */
    public Reservation reserve(Reservation reservation) {
        ReservableRoomId reservableRoomId = reservation.getReservableRoom().getReservableRoomId();
        // 悲観ロック
        ReservableRoom reservableRoom = reservableRoomRepository.findOneForUpdateByReservableRoomId(reservableRoomId);
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

    /**
     * 予約可能な部屋を取得.
     *
     * @param reservableRoomId
     * @return
     */
    public List<Reservation> findReservations(ReservableRoomId reservableRoomId) {
        return reservationRepository.findByReservableRoom_ReservableRoomIdOrderByStartTimeAsc(
                reservableRoomId);
    }

    /**
     * 予約のキャンセル処理.
     *
     * @param reservationId
     * @param requestUser
     */
    public void cancel(Integer reservationId, User requestUser) {
        Reservation reservation = reservationRepository.getOne(reservationId);
        if (RoleName.ADMIN != requestUser.getRoleName()
                && !Objects.equals(reservation.getUser().getUserId(), requestUser.getUserId())) {
            throw new AccessDeniedException("要求されたキャンセルは許可できません。");
        }
        reservationRepository.delete(reservation);
    }

    @PreAuthorize("hasRole('ADMIN') or #reservation.user.userId == principal.user.userId")
    public void cancel(@P("reservation") Reservation reservation) {
        reservationRepository.delete(reservation);
    }

    public Reservation getOne(Integer reservationId) {
        return reservationRepository.getOne(reservationId);
    }

}
