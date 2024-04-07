package com.crispy.crispyapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDto {
    private long id;
    private String name;
    private long workspaceId;
    private List<BoardColumnDto> columns = new ArrayList<>();
    @Data
    @NoArgsConstructor
    public static class BoardColumnDto {
        private long id;
        private String name;
        private List<BoardCardDto> cards = new ArrayList<>();
        public BoardColumnDto(long id, String name) {
            this.id = id;
            this.name = name;
        }
    }
    @Data
    @AllArgsConstructor
    public static class BoardCardDto {
        private long id;
        private String name;
    }
}
