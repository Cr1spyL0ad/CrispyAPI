package com.crispy.crispyapi.dto;

import com.crispy.crispyapi.model.Workspace;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private List<Workspace> workspaces;
}
