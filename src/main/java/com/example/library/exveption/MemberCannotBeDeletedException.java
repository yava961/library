package com.example.library.exveption;

public class MemberCannotBeDeletedException extends RuntimeException{
    public MemberCannotBeDeletedException(Long id) {
        super("Member cannot be deleted because they have borrowed books, id: " + id);
    }
}
