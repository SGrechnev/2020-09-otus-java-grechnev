package ru.otus.core.model;


import javax.persistence.*;

@Entity
@Table(name = "my_users")
public class User {

    @Id
    @Column(name = "login")
    private String login;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    private Role role;

    public User() {
    }

    public User(String login, String name, String password, Role role) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.role = role;
    }

    // Getters

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    // Setters

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    public enum Role {
        USER,
        ADMIN
    }
}
