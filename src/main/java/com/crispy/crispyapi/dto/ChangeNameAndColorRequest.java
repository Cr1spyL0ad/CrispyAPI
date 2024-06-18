package com.crispy.crispyapi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeNameAndColorRequest {
    private String color;
    private String name;
}
