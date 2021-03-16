package com.roginak.springboot.dao;

import com.roginak.springboot.entities.Role;

import java.util.List;

public interface RoleDao {
    List<Role> getAllRoles();

    Role getRoleByRoleName(String s);
}
