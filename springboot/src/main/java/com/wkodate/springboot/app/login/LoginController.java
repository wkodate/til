package com.wkodate.springboot.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by wkodate on 2018/11/13.
 */
@Controller
public class LoginController {
    @RequestMapping("loginForm")
    String loginForm() {
        return "login/loginForm";
    }
}
