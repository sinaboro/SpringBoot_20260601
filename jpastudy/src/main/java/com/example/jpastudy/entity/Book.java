package com.example.jpastudy.entity;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter @Setter
@NoArgsConstructor
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String author;
    private int price;
    private int stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    public Book(String title, String author, int price, int stock, Category category)
    {
        this.title = title;
        this.author = author;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
}