package com.roginak.springboot.service;

import com.roginak.springboot.dao.UserDao;
import com.roginak.springboot.entities.Role;
import com.roginak.springboot.entities.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserDao userDao;
    private final RoleService roleService;

    public UserServiceImpl(UserDao userDao, RoleService rs) {
        this.userDao = userDao;
        this.roleService = rs;
    }


    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        return userDao.getUserByLogin(login);
    }

    @Override
    @Transactional
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        setUserRoles(user);
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userDao.saveUser(user);
    }

    @Override
    @Transactional
    public void editUser(User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        setUserRoles(user);
        userDao.editUser(user);
    }

    @Override
    @Transactional
    public User getUser(int id) {
        return userDao.getUser(id);
    }

    @Override
    public User getUserByLogin(String login) {
        return userDao.getUserByLogin(login);
    }

    @Transactional
    public void setUserRoles(User user) {
        if (user.getRoles() == null) {
            user.setRoles(new LinkedHashSet<Role>());
        }
        user.getListRoles().forEach(x -> user.getRoles().add(roleService.getRoleByRoleName(x)));
    }


    @Override
    @Transactional
    public void deleteUser(int id) {
        userDao.deleteUser(id);
    }
}
