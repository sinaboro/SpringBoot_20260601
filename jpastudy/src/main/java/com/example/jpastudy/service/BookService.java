package com.example.jpastudy.service;

import com.example.jpastudy.dto.*;
import com.example.jpastudy.entity.*;
import com.example.jpastudy.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    @Transactional
    public BookResponseDto createBook(BookRequestDto dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("카테고리를 찾을 수 없습니다."));
        Book book = new Book(dto.getTitle(), dto.getAuthor(),
                dto.getPrice(), dto.getStock(), category);
        return new BookResponseDto(bookRepository.save(book));
    }
    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAllWithCategory().stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());
    }
    public BookResponseDto getBook(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
        return new BookResponseDto(book);
    }
    public List<BookResponseDto> searchBooks(String keyword) {
        return bookRepository.findByTitleContaining(keyword).stream()
                .map(BookResponseDto::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public BookResponseDto updateBook(Long id, BookRequestDto dto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("도서를 찾을 수 없습니다."));
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setPrice(dto.getPrice());
        book.setStock(dto.getStock());
        return new BookResponseDto(book); // 변경 감지로 자동 UPDATE
    }
    @Transactional
    public void deleteBook(Long id) {
        bookRepository.deleteById(id);
    }
}