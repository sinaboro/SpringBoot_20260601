package com.example.jpastudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    // mappedBy = "member" : Order.member 필드가 연관관계의 주인
    // cascade = ALL : 회원 삭제 시 주문도 함께 삭제
    // orphanRemoval = true : 컬렉션에서 제거 시 DB에서도 삭제
    @OneToMany(mappedBy = "member",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    // 편의 생성자
    public Member(String username, String email, int age) {
        this.username = username;
        this.email = email;
        this.age = age;
    }
}