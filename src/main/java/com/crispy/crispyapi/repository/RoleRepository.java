package com.crispy.crispyapi.repository;

import com.crispy.crispyapi.model.Role;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByUserAndWorkspace(User user, Workspace workspace);
    boolean existsRoleByUserAndWorkspace(User user, Workspace workspace);
}
