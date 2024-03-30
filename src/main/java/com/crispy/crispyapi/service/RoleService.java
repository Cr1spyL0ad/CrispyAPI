package com.crispy.crispyapi.service;

import com.crispy.crispyapi.model.Role;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.model.Workspace;
import com.crispy.crispyapi.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements ServiceInterface<Role> {
    private final RoleRepository roleRepository;
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
    @Override
    public boolean create(Role role) {
        roleRepository.save(role);
        return true;
    }

    @Override
    public Role read(Long id) throws Exception {
        return null;
    }
    public Role read(User user, Workspace workspace) throws Exception {
        return roleRepository.findRoleByUserAndWorkspace(user, workspace);
    }

    @Override
    public List<Role> readAll() {
        return null;
    }

    @Override
    public boolean update(Role role) {
        try {
            roleRepository.save(role);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean delete(Long id) throws Exception {
        try {
            roleRepository.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean deleteAll() {
        return false;
    }
    public boolean isUserAdmin(User user, Workspace workspace) {
        return roleRepository.findRoleByUserAndWorkspace(user, workspace).isAdmin();
    }
    public boolean isUserExists(User user, Workspace workspace) {
        return roleRepository.existsRoleByUserAndWorkspace(user, workspace);
    }
}
