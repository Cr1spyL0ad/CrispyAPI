package com.crispy.crispyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkspaceUserDto {
    private Long id;
    private String name;
    private boolean isAdmin;
}
