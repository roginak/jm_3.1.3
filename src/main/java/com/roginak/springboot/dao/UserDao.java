package com.roginak.springboot.dao;



import com.roginak.springboot.entities.Role;
import com.roginak.springboot.entities.User;

import java.util.List;

public interface UserDao {

    List<User> getAllUsers();

    public void saveUser(User user);

    public void editUser(User user);

    public User getUser(int id);

    public void deleteUser(int id);

    public User getUserByLogin(String username);

}
