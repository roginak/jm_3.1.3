package com.roginak.springboot.service;



import com.roginak.springboot.entities.Role;
import com.roginak.springboot.entities.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    public void saveUser(User user);

    public void editUser(User user);

    public User getUser(int id);

    public User getUserByLogin(String login);

    public void deleteUser(int id);


}
