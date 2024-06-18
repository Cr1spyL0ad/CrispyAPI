package com.crispy.crispyapi.dto;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private List<UserWorkspaceDto> workspaces;
    @Data
    @AllArgsConstructor
    public static class UserWorkspaceDto {
        private Long id;
        private String name;
        private String color;
        private int users;
    }
}

