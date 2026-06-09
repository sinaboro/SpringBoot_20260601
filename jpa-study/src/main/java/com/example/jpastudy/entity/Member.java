package com.example.jpastudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "members")
@Getter @Setter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
    private Long id;

    @Column(name = "user_name", nullable = false, length = 50)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    private int age;

    // 편의 생성자
    public Member(String username, String email, int age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }
}