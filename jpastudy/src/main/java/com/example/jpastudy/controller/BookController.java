package com.example.jpastudy.controller;

import com.example.jpastudy.dto.*;
import com.example.jpastudy.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponseDto> create(@RequestBody BookRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createBook(dto));
    }
    @GetMapping
    public ResponseEntity<List<BookResponseDto>> getAll() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponseDto> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }
    @GetMapping("/search")
    public ResponseEntity<List<BookResponseDto>> search(@RequestParam String keyword)
    {
        return ResponseEntity.ok(bookService.searchBooks(keyword));
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookResponseDto> update(
            @PathVariable Long id, @RequestBody BookRequestDto dto) {
        return ResponseEntity.ok(bookService.updateBook(id, dto));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}