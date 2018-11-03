package com.wkodate.springboot.domain.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by wkodate on 2018/10/24.
 */
@Entity
@Table(name = "user")
@Getter
@Setter
public class User {

    @Id
    private String userId;

    private String password;

    private String firstName;

    private String lastName;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

}
