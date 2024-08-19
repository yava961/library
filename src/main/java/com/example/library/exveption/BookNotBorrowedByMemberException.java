package com.example.library.exveption;

public class BookNotBorrowedByMemberException extends RuntimeException{
    public BookNotBorrowedByMemberException(Long memberId, Long bookId) {
        super("Book with id " + bookId + " was not borrowed by member with id " + memberId);
    }
}
