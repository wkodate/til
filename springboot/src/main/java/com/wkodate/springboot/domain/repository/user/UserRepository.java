package com.wkodate.springboot.domain.repository.user;

import com.wkodate.springboot.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by wkodate on 2018/11/13.
 */
public interface UserRepository extends JpaRepository<User, String> {
}
