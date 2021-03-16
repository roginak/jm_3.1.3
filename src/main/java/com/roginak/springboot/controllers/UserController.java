package com.roginak.springboot.controllers;

import com.roginak.springboot.entities.User;
import com.roginak.springboot.service.RoleService;
import com.roginak.springboot.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }


    @GetMapping(value = "login")
    public String loginPage(ModelMap map) {
        map.addAttribute("msg", "Successfully logged in");

        return "login";
    }

    @GetMapping("/user")
    public String getUserInfo(Model model, Principal p) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("user", userService.getUserByLogin(p.getName()));
        model.addAttribute("roleService", roleService);
        return "user/info";
    }

    @RequestMapping(value = "admin")
    public String adminPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User cur_user = userService.getUserByLogin(authentication.getName());
        model.addAttribute("user", cur_user);
        model.addAttribute("roleService", roleService);
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("new_user", new User());

        return "admin/all";
    }


    @PostMapping("/admin/add")
    public String create(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/admin/delete")
    public String deleteUser(@ModelAttribute("cur_user") User user) {
        int id = user.getId();
        userService.deleteUser(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = (User) auth.getPrincipal();
        if (us.getId() == id) {
            return "redirect:/login";
        }

        return "redirect:/admin";
    }

    @PostMapping("/admin/edit")
    public String edit(@ModelAttribute("cur_user") User user) {
        userService.editUser(user);

        int id = user.getId();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User us = (User) auth.getPrincipal();
        if (us.getId() == id) {
            us.setLogin(user.getLogin());
            if (user.getRoles().stream()
                    .anyMatch(r -> r.getRoleName().equals("admin"))) {
                return "redirect:/admin";
            } else {
                return "redirect:/login";
            }
        }

        return "redirect:/admin";
    }


}