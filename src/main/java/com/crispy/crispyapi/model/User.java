package com.crispy.crispyapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@ToString
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @jakarta.persistence.Column(unique = true, nullable = false)
    private Long id;
    @jakarta.persistence.Column(unique = true, nullable = false)
    private String username;
    @jakarta.persistence.Column(nullable = false)
    private String password;
    @jakarta.persistence.Column(nullable = false)
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @JoinTable(name = "users_workspaces",joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "workspace_id"))
    private Set<Workspace> workspaces = new HashSet<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE)
    private List<Role> roles = new ArrayList<>();

    public void addWorkspace(Workspace workspace){
        workspaces.add(workspace);
        workspace.getUsers().add(this);
    }

    public void removeWorkspace(Workspace workspace){
        workspaces.remove(workspace);
        workspace.getUsers().remove(this);
    }
    @PreRemove
    private void removeAssociations() {
        for (Workspace workspace: this.workspaces) {
            workspace.getUsers().remove(this);
        }
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new SimpleGrantedAuthority("user"));
        return list;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
