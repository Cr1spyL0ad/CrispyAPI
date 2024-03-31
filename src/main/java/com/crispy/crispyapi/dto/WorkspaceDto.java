package com.crispy.crispyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class WorkspaceDto {
    private long id;
    private String name;
    private List<WorkspaceUserDto> users;
    private List<WorkspaceBoardDto> boards;

    @Data
    @AllArgsConstructor
    public static class WorkspaceUserDto {
        private Long id;
        private String name;
        private boolean isAdmin;
    }

    @Data
    @AllArgsConstructor
    public static class WorkspaceBoardDto {
        private long id;
        private String name;
    }
}
