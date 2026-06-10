package com.example.jpastudy;

import com.example.jpastudy.entity.*;
import com.example.jpastudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    @Transactional
    public void run(String... args) {
        Category it = categoryRepository.save(new Category(null, "IT/개발", null));
        Category novel = categoryRepository.save(new Category(null, "소설", null));
        bookRepository.save(new Book("자바의 정석", "남궁성", 32000, 50, it));
        bookRepository.save(new Book("스프링 부트 핵심 가이드", "장정우", 28000, 30,
                it));
        bookRepository.save(new Book("클린 코드", "로버트 마틴", 26000, 20, it));
        bookRepository.save(new Book("채식주의자", "한강", 14000, 15, novel));
        System.out.println("✅ 초기 데이터 삽입 완료!");
    }
}