package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "workspaces")
@Data
public class Workspace {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    private String name;
    private String color = "C35BE8";

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "workspaces", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<User> users = new HashSet<>();
    @PreRemove
    private void removeAssociations() {
        for (User user: this.users) {
            user.getWorkspaces().remove(this);
        }
    }
}
