package com.iulian.FinalProject.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String email;
    private String password;

    @ManyToOne
    @JoinColumn(name = "fk_role_id")
    private Role role;

//    public User(String username, String email, String password) {
//        this.username = username;
//        this.email = email;
//        this.password = password;
//    }

}
