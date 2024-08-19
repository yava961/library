package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitleAndAuthor(String title, String author);

    @Query(value = "SELECT DISTINCT b.title FROM book b WHERE b.member_id IS NOT NULL", nativeQuery = true)
    List<String> findDistinctBorrowedBookTitles();

    @Query(value = "SELECT b.title, COUNT(b.id) FROM book b WHERE b.member_id IS NOT NULL GROUP BY b.title", nativeQuery = true)
    List<Object[]> findDistinctBorrowedBookTitlesWithCount();
}
