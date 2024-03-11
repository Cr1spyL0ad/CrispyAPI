package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "workspaces")
@Data
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    private String name;
    @OneToMany
    @JoinColumn(name = "workspace_id")
    private List<Board> boards;
    @ManyToMany(mappedBy = "workspaces")
    private List<User> users;

}
