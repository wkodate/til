package com.wkodate.springboot.app.reservation;

import com.wkodate.springboot.domain.model.ReservableRoom;
import com.wkodate.springboot.domain.model.ReservableRoomId;
import com.wkodate.springboot.domain.model.Reservation;
import com.wkodate.springboot.domain.model.User;
import com.wkodate.springboot.domain.service.reservation.AlreadyReservedException;
import com.wkodate.springboot.domain.service.reservation.ReservationService;
import com.wkodate.springboot.domain.service.reservation.UnavailableReservationException;
import com.wkodate.springboot.domain.service.room.RoomService;
import com.wkodate.springboot.domain.service.user.ReservationUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by wkodate on 2018/11/06.
 */
@Controller
@RequestMapping("reservations/{date}/{roomId}")
public class ReservationsController {
    @Autowired
    RoomService roomService;

    @Autowired
    ReservationService reservationService;

    @ModelAttribute
    ReservationForm setUpForm() {
        ReservationForm form = new ReservationForm();
        form.setStartTime(LocalTime.of(9, 0));
        form.setEndTime(LocalTime.of(10, 0));
        return form;
    }

    /**
     * 予約フォーム.
     *
     * @param date
     * @param roomId
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    String reserveForm(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                       @PathVariable("date") LocalDate date,
                       @PathVariable("roomId") Integer roomId,
                       Model model) {
        ReservableRoomId reservableRoomId = new ReservableRoomId(roomId, date);
        List<Reservation> reservations = reservationService.findReservations(reservableRoomId);

        List<LocalTime> timeList =
                Stream.iterate(LocalTime.of(0, 0), t -> t.plusMinutes(30))
                        .limit(24 * 2)
                        .collect(Collectors.toList()
                        );

        model.addAttribute("room", roomService.findMeetingRoom(roomId));
        model.addAttribute("reservations", reservations);
        model.addAttribute("timeList", timeList);
        // model.addAttribute("user", dummyUser());
        return "reservation/reserveForm";
    }

    /**
     * 予約.
     *
     * @param form
     * @param bindingResult
     * @param date
     * @param roomId
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    String reserve(@Validated ReservationForm form,
                   BindingResult bindingResult,
                   @AuthenticationPrincipal ReservationUserDetails userDetails,
                   @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
                   @PathVariable("roomId") Integer roomId,
                   Model model) {
        if (bindingResult.hasErrors()) {
            return reserveForm(date, roomId, model);
        }
        ReservableRoom reservableRoom = new ReservableRoom(new ReservableRoomId(roomId, date));
        Reservation reservation = new Reservation();
        reservation.setStartTime(form.getStartTime());
        reservation.setEndTime(form.getEndTime());
        reservation.setReservableRoom(reservableRoom);
        reservation.setUser(userDetails.getUser());
        try {
            reservationService.reserve(reservation);
        } catch (UnavailableReservationException | AlreadyReservedException e) {
            model.addAttribute("error", e.getMessage());
            return reserveForm(date, roomId, model);
        }
        return "redirect:/reservations/{date}/{roomId}";
    }

    /**
     * 予約キャンセル.
     *
     * @param reservationId
     * @param roomId
     * @param date
     * @param model
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, params = "cancel")
    String cancel(@RequestParam("reservationId") Integer reservationId,
                  @AuthenticationPrincipal ReservationUserDetails userDetails,
                  @PathVariable("roomId") Integer roomId,
                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @PathVariable("date") LocalDate date,
                  Model model) {
        User user = userDetails.getUser();
        try {
            reservationService.cancel(reservationId, user);
        } catch (AccessDeniedException e) {
            model.addAttribute("error", e.getMessage());
            return reserveForm(date, roomId, model);
        }
        return "redirect:/reservations/{date}/{roomId}";
    }

}
