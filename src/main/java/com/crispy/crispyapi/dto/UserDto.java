package com.crispy.crispyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private List<UserWorkspaceDto> workspaces;
    @Data
    @AllArgsConstructor
    public static class UserWorkspaceDto {
        private Long id;
        private String name;
    }
}

