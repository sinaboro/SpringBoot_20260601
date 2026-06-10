package com.example.jpastudy.repository;

import com.example.jpastudy.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String keyword);
    List<Book> findByCategoryId(Long categoryId);
    List<Book> findByPriceLessThanEqual(int maxPrice);
    // N+1 방지: JOIN FETCH로 Category 함께 조회
    @Query("SELECT b FROM Book b JOIN FETCH b.category")
    List<Book> findAllWithCategory();
}