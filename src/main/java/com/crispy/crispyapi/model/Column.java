package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "columns")
public class Column {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private int position;

    @OneToMany(mappedBy = "column", cascade = CascadeType.REMOVE)
    private List<Card> cards;

    @ManyToOne(targetEntity = Board.class)
    @JoinColumn(name = "board_id")
    private Board board;



    public Column(String name, Board board, int position) {
        this.name = name;
        this.board = board;
        this.position = position;
    }
}
