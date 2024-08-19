package com.example.library.web;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> addBook(@RequestBody @Valid BookDTO bookDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.saveBook(bookDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/borrowed/distinct-titles")
    public ResponseEntity<List<String>> getDistinctBorrowedBookTitles() {
        List<String> titles = bookService.getDistinctBorrowedBookTitles();
        if (titles.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(titles);
    }

    @GetMapping("/borrowed/distinct-titles-with-count")
    public ResponseEntity<List<Map<String, Object>>> getDistinctBorrowedBookTitlesWithCount() {
        List<Map<String, Object>> books = bookService.getDistinctBorrowedBookTitlesWithCount();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }
}
