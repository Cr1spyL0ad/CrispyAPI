package com.crispy.crispyapi.controller;

import com.crispy.crispyapi.model.Role;
import com.crispy.crispyapi.model.User;
import com.crispy.crispyapi.model.Workspace;
import com.crispy.crispyapi.service.RoleService;
import com.crispy.crispyapi.service.UserService;
import com.crispy.crispyapi.service.WorkspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
public class WorkspaceController {
    private final WorkspaceService workspaceService;
    private final RoleService roleService;
    private final UserService userService;
    @Autowired
    public WorkspaceController(WorkspaceService workspaceService, RoleService roleService, UserService userService) {
        this.workspaceService = workspaceService;
        this.roleService = roleService;
        this.userService = userService;
    }

    @GetMapping("/workspaces/{id}")
    public ResponseEntity<?> getWorkspace(@PathVariable Long id, @AuthenticationPrincipal User user) {
        try {
            Workspace workspace = workspaceService.read(id);
            if(!roleService.isUserExists(user, workspace))
                return ResponseEntity.badRequest().body("Can't find this user in this workspace");
            return ResponseEntity.ok(workspaceService.convertToDto(workspace));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
    @PostMapping("/workspaces")
    public ResponseEntity<?> createWorkspace(@AuthenticationPrincipal User user, @RequestBody String name) {
        if(user.getWorkspaces().size() >= 5)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't have more than 5 workspaces");
        Workspace workspace = new Workspace();
        workspace.setName(name);
        workspace.getUsers().add(user);
        workspaceService.create(workspace);
        user.addWorkspace(workspace);
        userService.update(user);
        roleService.create(new Role(workspace, user, true));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<?> deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal User user) {
        try {
            Workspace workspace = workspaceService.read(workspaceId);
            if(roleService.isUserExists(user, workspace)){
                if(roleService.isUserAdmin(user, workspace)){
                    workspaceService.delete(workspaceId);
                    return ResponseEntity.ok().build();
                }
                else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't delete this workspace because you aren't admin");
                }
            }
            else {
                return ResponseEntity.badRequest().body("Can't find this user in this workspace");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
    @PatchMapping("/workspaces/{workspaceId}")
    public ResponseEntity<?> renameWorkspace(@PathVariable Long workspaceId, @RequestBody String name, @AuthenticationPrincipal User user) {
        try {
            Workspace workspace = workspaceService.read(workspaceId);
            if(!roleService.isUserAdmin(user,workspace))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't do this because you aren't admin");
            workspace.setName(name);
            workspaceService.update(workspace);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Workspace not found");
        }
    }



    @PutMapping("/workspaces/{workspaceId}/users/{username}")
    public ResponseEntity<?> addUserToWorkspace(@PathVariable Long workspaceId, @PathVariable String username, @AuthenticationPrincipal User currentUser) {
        try {
            Workspace workspace = workspaceService.read(workspaceId);
            if(!roleService.isUserAdmin(currentUser,workspace))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't do this because you aren't admin");
            User userFromRequest = (User) userService.loadUserByUsername(username);
            if(workspace.getUsers().contains(userFromRequest)) {
                return ResponseEntity.badRequest().body("This user already exists in this workspace");
            }
            userFromRequest.addWorkspace(workspace);
            userService.update(userFromRequest);
            roleService.create(new Role(workspace, userFromRequest, false));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }

    }
    @PatchMapping("/workspaces/{workspaceId}/users/{userId}")
    public ResponseEntity<?> changeUserRoleInWorkspace(@PathVariable Long workspaceId, @PathVariable Long userId, @AuthenticationPrincipal User currentUser) {
        try {
            Workspace workspace = workspaceService.read(workspaceId);
            if(!roleService.isUserAdmin(currentUser,workspace))
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't do this because you aren't admin");
            User userFromRequest = userService.read(userId);
            Role role = roleService.read(userFromRequest, workspace);
            role.setAdmin(!role.isAdmin());
            roleService.update(role);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }

    }
    @DeleteMapping("/workspaces/{workspaceId}/users/{userId}")
    public ResponseEntity<?> deleteUserFromWorkspace(@PathVariable Long workspaceId, @PathVariable Long userId, @AuthenticationPrincipal User currentUser) {
        try {
            User user = userService.read(userId);
            Workspace workspace = workspaceService.read(workspaceId);
            if(!roleService.isUserExists(user, workspace))
                return ResponseEntity.badRequest().body("Can't find this user in this workspace");
            if(roleService.isUserAdmin(user,workspace) || user.equals(currentUser)) {
                user.removeWorkspace(workspace);
                userService.update(user);
                roleService.delete(roleService.read(user, workspace).getId());
                if(roleService.getAllAdmins(workspace).isEmpty()) {
                    workspaceService.delete(workspace.getId());
                }
                return ResponseEntity.ok().build();
            }
            else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can't do this because you aren't admin");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getLocalizedMessage());
        }
    }
}
