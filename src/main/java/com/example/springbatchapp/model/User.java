package com.example.springbatchapp.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long num;
    private String userName;
    private String password;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.REMOVE }, fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();

    public User() {
    }

    public User(Long num, String userName, String password) {
        this.num = num;
        this.userName = userName;
        this.password = password;
    }

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User{" +
                "num=" + num +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
