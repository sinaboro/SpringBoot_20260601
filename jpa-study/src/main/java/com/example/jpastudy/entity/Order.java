package com.example.jpastudy.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String product;

    private int price;

    // ── 연관관계 핵심 ──────────────────────────
    // 여러 주문(Many)이 한 회원(One)에 속함

    //@JoinColumn(name = "member_id") // FK 컬럼명 지정

    @ManyToOne(fetch = FetchType.LAZY) // LAZY = 실제 사용 시점에 쿼리
    @JoinColumn(name = "member_id")
    private Member member;

    public Order(String product, int price, Member member) {
        this.product = product;
        this.price = price;
        this.member = member;
    }
}