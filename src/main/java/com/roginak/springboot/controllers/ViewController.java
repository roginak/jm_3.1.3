package com.roginak.springboot.controllers;

import com.roginak.springboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class ViewController {

    private final UserService userService;

    public ViewController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("user")
    public String getUserInfo(HttpServletResponse response, Principal p) {
        int id = userService.getUserByLogin(p.getName()).getId();
        response.addCookie(new Cookie("currentUserId", Integer.toString(id)));
        response.addCookie(new Cookie("currentUserLogin", p.getName()));
        return "user/info";
    }

    @RequestMapping(value = "admin")
    public String adminPage(HttpServletResponse response, Principal p) {
        int id = userService.getUserByLogin(p.getName()).getId();
        response.addCookie(new Cookie("currentUserId", Integer.toString(id)));
        response.addCookie(new Cookie("currentUserLogin", p.getName()));
        return "admin/main";
    }

}