package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    private boolean isAdmin;
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(targetEntity = Workspace.class)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    public Role(Workspace workspace, User user, boolean isAdmin) {
        this.user = user;
        this.workspace = workspace;
        this.isAdmin = isAdmin;
    }
}
