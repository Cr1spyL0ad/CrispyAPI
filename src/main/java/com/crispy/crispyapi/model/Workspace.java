package com.crispy.crispyapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.REMOVE)
    private List<Role> roles = new ArrayList<>();

    @ManyToMany(mappedBy = "workspaces")
    @EqualsAndHashCode.Exclude
    private Set<User> users = new HashSet<>();
    @PreRemove
    private void removeAssociations() {
        for (User user: this.users) {
            user.getWorkspaces().remove(this);
        }
    }
}
