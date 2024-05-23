package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private String description;
    private LocalDateTime creationTime;
    private LocalDateTime deadLineTime;
    private int storyPoints;

    @ManyToOne(targetEntity = Column.class)
    @JoinColumn(name = "column_id")
    private Column column;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
}
