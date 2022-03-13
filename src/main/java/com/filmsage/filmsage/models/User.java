package com.filmsage.filmsage.models;

import com.filmsage.filmsage.models.auth.Role;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private UserContent userContent = new UserContent(this);

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 64, unique = true)
    private String username;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "token_expired")
    private boolean tokenExpired;

    @ManyToMany
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private java.util.Collection<Role> roles;

    public User() {
        super();
        this.enabled = false;
    }

    public User(String email, String username, String password, Timestamp createdAt) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.createdAt = createdAt;
//        this.enabled = false;
    }

    public User(User copy) {
        id = copy.id;
        userContent = copy.userContent;
        email = copy.email;
        username = copy.username;
        password = copy.password;
        createdAt = copy.createdAt;
        enabled = copy.enabled;
        tokenExpired = copy.tokenExpired;
        roles = copy.roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserContent getUserContent() {
        return userContent;
    }

    public void setUserContent(UserContent userContent) {
        this.userContent = userContent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTokenExpired() {
        return tokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        this.tokenExpired = tokenExpired;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
