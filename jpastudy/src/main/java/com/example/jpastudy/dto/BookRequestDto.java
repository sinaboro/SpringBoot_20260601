package com.example.jpastudy.dto;

import lombok.Getter;
import lombok.Setter;
@Getter @Setter
public class BookRequestDto {
    private String title;
    private String author;
    private int price;
    private int stock;
    private Long categoryId;
}