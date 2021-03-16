package com.roginak.springboot.service;

import com.roginak.springboot.entities.Role;
import com.roginak.springboot.entities.User;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    public String getStringWithRoles(User user);
    Role getRoleByRoleName(String s);
}
