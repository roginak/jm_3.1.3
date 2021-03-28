package com.roginak.springboot.controllers;

import com.roginak.springboot.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class ViewController {

    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("user")
    public String getUserInfo(HttpServletResponse response, Principal p) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        response.addCookie(new Cookie("currentUserId", Integer.toString(user.getId())));
        response.addCookie(new Cookie("currentUserLogin", p.getName()));
        return "user/info";
    }

    @RequestMapping(value = "admin")
    public String adminPage(HttpServletResponse response, Principal p) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        response.addCookie(new Cookie("currentUserId", Integer.toString(user.getId())));
        response.addCookie(new Cookie("currentUserLogin", p.getName()));
        return "admin/main";
    }

}