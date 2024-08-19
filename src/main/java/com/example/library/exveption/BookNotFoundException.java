package com.example.library.exveption;

public class BookNotFoundException extends RuntimeException{
    public BookNotFoundException(Long id) {
        super("Book not found with id: " + id);
    }
}
