package com.example.jpastudy.dto;

import com.example.jpastudy.entity.Book;
import lombok.Getter;
@Getter
public class BookResponseDto {
    private Long id;
    private String title;
    private String author;
    private int price;
    private int stock;
    private String categoryName;
    public BookResponseDto(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.price = book.getPrice();
        this.stock = book.getStock();
        this.categoryName = book.getCategory() != null
                ? book.getCategory().getName() : null;
    }
}