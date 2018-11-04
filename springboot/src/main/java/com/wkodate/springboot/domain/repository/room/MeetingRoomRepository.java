package com.wkodate.springboot.domain.repository.room;

import com.wkodate.springboot.domain.model.MeetingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wkodate on 2018/11/04.
 */
public interface MeetingRoomRepository extends JpaRepository<MeetingRoom, Integer> {
}
