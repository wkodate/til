package com.wkodate.springboot.domain.service.user;

import com.wkodate.springboot.domain.model.User;
import com.wkodate.springboot.domain.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by wkodate on 2018/11/13.
 */
public class ReservationUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getOne(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " is not found.");
        }
        return new ReservationUserDetails(user);
    }
}
