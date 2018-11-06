package com.wkodate.springboot.domain.service.room;

import com.wkodate.springboot.domain.model.MeetingRoom;
import com.wkodate.springboot.domain.model.ReservableRoom;
import com.wkodate.springboot.domain.repository.room.MeetingRoomRepository;
import com.wkodate.springboot.domain.repository.room.ReservableRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by wkodate on 2018/10/30.
 */
@Service
@Transactional
public class RoomService {
    @Autowired
    ReservableRoomRepository reservableRoomRepository;

    @Autowired
    MeetingRoomRepository meetingRoomRepository;

    /**
     * 予約可能な部屋一覧を返す.
     *
     * @param localDate
     * @return
     */
    public List<ReservableRoom> findReservableRooms(LocalDate localDate) {
        return reservableRoomRepository.findByReservableRoomId_reservedDateOrderByReservableRoomId_roomIdAsc(localDate);
    }

    /**
     * 会議室情報を取得.
     *
     * @param roomId
     * @return
     */
    public MeetingRoom findMeetingRoom(Integer roomId) {
        return meetingRoomRepository.getOne(roomId);
    }

}
