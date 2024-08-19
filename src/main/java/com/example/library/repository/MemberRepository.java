package com.example.library.repository;

import com.example.library.entity.Book;
import com.example.library.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT b FROM Book b JOIN b.member m WHERE m.name LIKE :name")
    List<Book> findBorrowedBooksByMemberName(@Param("name") String name);

    @Query("SELECT m.id FROM Member m WHERE m.name LIKE :name")
    Optional<Long> findIdByName(String name);
}
