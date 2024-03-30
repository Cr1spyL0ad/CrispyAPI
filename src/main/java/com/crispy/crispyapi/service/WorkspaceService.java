package com.crispy.crispyapi.service;

import com.crispy.crispyapi.dto.WorkspaceBoardDto;
import com.crispy.crispyapi.dto.WorkspaceDto;
import com.crispy.crispyapi.dto.WorkspaceUserDto;
import com.crispy.crispyapi.model.Workspace;
import com.crispy.crispyapi.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class WorkspaceService implements ServiceInterface<Workspace> {
    private final WorkspaceRepository workspaceRepository;
    private final RoleService roleService;
    @Autowired
    public WorkspaceService(WorkspaceRepository workspaceRepository, RoleService roleService) {
        this.workspaceRepository = workspaceRepository;
        this.roleService = roleService;
    }
    @Override
    public boolean create(Workspace workspace) {
        workspaceRepository.save(workspace);
        return true;
    }

    @Override
    public Workspace read(Long id) throws Exception {
        return workspaceRepository.findWorkspaceById(id).orElseThrow(() -> new Exception("Workspace not found"));
    }

    @Override
    public List<Workspace> readAll() {
        return workspaceRepository.findAll();
    }

    @Override
    public boolean update(Workspace workspace) {
        try {
            workspaceRepository.save(workspace);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean delete(Long id) throws Exception {
        workspaceRepository.deleteWorkspaceById(id).orElseThrow(() -> new Exception("Workspace not found"));
        return true;
    }

    @Override
    public boolean deleteAll() {
        try {
            workspaceRepository.deleteAll();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public WorkspaceDto convertToDto(Workspace workspace) {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setId(workspace.getId());
        workspaceDto.setName(workspace.getName());
        workspaceDto.setUsers(new ArrayList<>());
        workspace.getUsers().forEach(user ->
            workspaceDto.getUsers().add(new WorkspaceUserDto(user.getId(), user.getName(), roleService.isUserAdmin(user, workspace)))
        );
        workspaceDto.setBoards(new ArrayList<>());
        workspace.getBoards().forEach(board ->
            workspaceDto.getBoards().add(new WorkspaceBoardDto(board.getId(), board.getName()))
        );
        return workspaceDto;
    }
}
