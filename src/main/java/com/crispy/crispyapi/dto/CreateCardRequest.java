package com.crispy.crispyapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
public class CreateCardRequest {
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDateTime creationTime;

    private LocalDateTime deadLineTime;
    private int storyPoints;
}
