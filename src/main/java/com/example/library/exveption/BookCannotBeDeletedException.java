package com.example.library.exveption;

public class BookCannotBeDeletedException extends RuntimeException{
    public BookCannotBeDeletedException(Long id) {
        super("Book cannot be deleted because it is borrowed, id: " + id);
    }
}
