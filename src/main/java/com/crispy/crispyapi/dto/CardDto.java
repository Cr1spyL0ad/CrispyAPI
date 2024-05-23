package com.crispy.crispyapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CardDto {
    private long id;
    private String name;
    private String description;
    private String userName;
    private long userId;
    @JsonFormat(pattern = "dd.MM.yy HH:mm")
    private LocalDateTime creationTime;
    private LocalDateTime deadLineTime;
    private int storyPoints;
}
