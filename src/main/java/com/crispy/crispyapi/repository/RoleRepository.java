package com.crispy.crispyapi.repository;

import com.crispy.crispyapi.model.Role;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findRoleByUserAndWorkspace(User user, Workspace workspace);
    Optional<Role> findById(Long id);
    Optional<Void> deleteRoleById(Long id);
    List<Role> findAllByWorkspaceAndIsAdmin(Workspace workspace, Boolean isAdmin);
    boolean existsRoleByUserAndWorkspace(User user, Workspace workspace);
}
