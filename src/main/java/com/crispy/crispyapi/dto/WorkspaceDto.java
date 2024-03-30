package com.crispy.crispyapi.dto;

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
}
