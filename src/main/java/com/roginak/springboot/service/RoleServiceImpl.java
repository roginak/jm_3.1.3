package com.roginak.springboot.service;

import com.roginak.springboot.dao.RoleDao;
import com.roginak.springboot.entities.Role;
import com.roginak.springboot.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleDao roleDao;

    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDao.getAllRoles();
    }

    @Override
    public String getStringWithRoles(User user) {
        final String[] res = {""};
        List<Role> sortedList = user.getRoles().stream().sorted().collect(Collectors.toList());
        sortedList.forEach(x -> res[0] += x.getRoleName() + " ");
        return res[0];
    }

    @Override
    public Role getRoleByRoleName(String rolename) {
        return roleDao.getRoleByRoleName(rolename);
    }
}
