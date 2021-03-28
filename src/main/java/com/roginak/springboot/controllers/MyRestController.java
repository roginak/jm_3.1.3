package com.roginak.springboot.controllers;

import com.roginak.springboot.entities.Role;
import com.roginak.springboot.entities.User;
import com.roginak.springboot.service.RoleService;
import com.roginak.springboot.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

@RequestMapping("/api")
@RestController
public class MyRestController {

    private final UserService userService;
    public final RoleService roleService;

    public MyRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @PatchMapping("/users/{id}")
    public ResponseEntity<?> update(@RequestBody User user, @PathVariable Integer id) {
        try {
            userService.editUser(user);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @PostMapping(value = "/users")
    public ResponseEntity<User> create(@RequestBody User user) {
        try {
            userService.getUserByLogin(user.getLogin());
            return new ResponseEntity<User>((User) null, HttpStatus.CONFLICT);
        } catch (Exception ignored) {

        }

        try {
            userService.saveUser(user);
            User addedUser = userService.getUserByLogin(user.getLogin());
            return new ResponseEntity<User>(addedUser, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<User>((User) null, HttpStatus.NOT_MODIFIED);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> read() {
        try {
            List<User> users = userService.getAllUsers();
            return (users != null) && (!users.isEmpty())
                    ? new ResponseEntity<>(users, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return (roles != null) && (!roles.isEmpty())
                    ? new ResponseEntity<>(roles, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/getLoginAndRolesString/{id}")
    public ResponseEntity<List<String>> getLoginAndRolesString(@PathVariable Integer id) {
        try {
            User user = userService.getUser(id);
            List<String> body = new LinkedList<>();
            body.add(user.getLogin());
            body.add(roleService.getStringWithRoles(user));
            return new ResponseEntity<>(body, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> read(@PathVariable("id") Integer id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (NoSuchElementException ex) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}