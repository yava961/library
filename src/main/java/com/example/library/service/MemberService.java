package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.dto.MemberDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Member;
import com.example.library.exveption.*;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BookRepository bookRepository;

    @Value("${library.member.bookLimit}")
    private int bookLimit;

    public MemberDTO saveMember(MemberDTO memberDTO) {
        Member member = new Member();
        member.setName(memberDTO.getName());
        member.setMembershipDate(LocalDate.now());
        return new MemberDTO(memberRepository.save(member));
    }

    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
        if (!member.getBooks().isEmpty()) {
            throw new MemberCannotBeDeletedException(id);
        }
        memberRepository.delete(member);
    }

    public MemberDTO getMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException(id));
        return new MemberDTO(member);
    }

    public List<MemberDTO> getAllMembers() {
        return memberRepository.findAll().stream().map(MemberDTO::new).collect(Collectors.toList());
    }

    public void borrowBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        if (member.getBooks().size() >= bookLimit) {
            throw new BookLimitExceededException(memberId);
        }

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));
        if (book.getAmount() == 0) {
            throw new BookOutOfStockException(bookId);
        }

        book.setAmount(book.getAmount() - 1);
        book.setMember(member);

        member.getBooks().add(book);

        memberRepository.save(member);
    }

    public void returnBook(Long memberId, Long bookId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new MemberNotFoundException(memberId));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new BookNotFoundException(bookId));

        book.setMember(null);
        book.setAmount(book.getAmount() + 1);

        bookRepository.save(book);
        memberRepository.save(member);
    }

    public List<BookDTO> findBorrowedBooksByMemberName(String name) {
        Optional<Long> memberId = memberRepository.findIdByName(name);
        if(memberId.isEmpty()){
            throw new RuntimeException();
        }
        List<Book> books = memberRepository.findBorrowedBooksByMemberName(name);
        return books.stream()
                .map(book -> new BookDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getAmount(), memberId.get()))
                .toList();
    }
}
