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
        return roleRepository.findById(id).orElseThrow(() -> new Exception("Role not found"));
    }
    public Role read(User user, Workspace workspace) {
        return roleRepository.findRoleByUserAndWorkspace(user, workspace);
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
            roleRepository.deleteRoleById(id).orElseThrow(() -> new Exception("Role not found"));
            return true;
    }

    public boolean isUserAdmin(User user, Workspace workspace) {
        return roleRepository.findRoleByUserAndWorkspace(user, workspace).isAdmin();
    }
    public boolean isUserExists(User user, Workspace workspace) {
        return roleRepository.existsRoleByUserAndWorkspace(user, workspace);
    }
    public List<Role> getAllAdmins(Workspace workspace) {
        return roleRepository.findAllByWorkspaceAndIsAdmin(workspace, true);
    }
}
