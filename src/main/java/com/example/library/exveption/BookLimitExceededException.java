package com.example.library.exveption;

public class BookLimitExceededException extends RuntimeException{
    public BookLimitExceededException(Long memberId) {
        super("Member with id " + memberId + " has reached the book limit.");
    }
}
