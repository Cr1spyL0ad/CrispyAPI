package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    private String name;
    @OrderBy(value = "position")
    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Column> columns = new ArrayList<>();

    @ManyToOne(targetEntity = Workspace.class)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    public Board(String name, Workspace workspace) {
        this.name = name;
        this.workspace = workspace;
    }
}
