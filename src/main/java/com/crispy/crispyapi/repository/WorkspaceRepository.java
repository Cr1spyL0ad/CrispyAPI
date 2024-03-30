package com.crispy.crispyapi.repository;

import com.crispy.crispyapi.model.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
    Optional<Workspace> findWorkspaceById(Long id);
    Optional<Void> deleteWorkspaceById(Long id);
}
