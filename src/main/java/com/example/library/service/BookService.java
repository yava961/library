package com.example.library.service;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Member;
import com.example.library.exveption.BookCannotBeDeletedException;
import com.example.library.exveption.BookNotFoundException;
import com.example.library.exveption.MemberNotFoundException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private MemberRepository memberRepository;

    public BookDTO saveBook(BookDTO bookDTO) {
        Optional<Book> existingBook = bookRepository.findByTitleAndAuthor(bookDTO.getTitle(), bookDTO.getAuthor());
        if (existingBook.isPresent()) {
            Book book = existingBook.get();
            book.setAmount(book.getAmount() + 1);
            return new BookDTO(bookRepository.save(book));
        }
        Book newBook = new Book();
        newBook.setTitle(bookDTO.getTitle());
        newBook.setAuthor(bookDTO.getAuthor());
        newBook.setAmount(bookDTO.getAmount());

        if (bookDTO.getMemberId() != null) {
            Member member = memberRepository.findById(bookDTO.getMemberId())
                    .orElseThrow(() -> new MemberNotFoundException(bookDTO.getMemberId()));
            newBook.setMember(member);
        }

        return new BookDTO(bookRepository.save(newBook));
    }

    public void deleteBook(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        if (book.getMember() != null) {
            throw new BookCannotBeDeletedException(id);
        }
        bookRepository.delete(book);
    }

    public BookDTO getBookById(Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
        return new BookDTO(book);
    }

    public List<BookDTO> getAllBooks() {
        return bookRepository.findAll().stream().map(BookDTO::new).collect(Collectors.toList());
    }

    public List<String> getDistinctBorrowedBookTitles() {
        return bookRepository.findDistinctBorrowedBookTitles();
    }
    public List<Map<String, Object>> getDistinctBorrowedBookTitlesWithCount() {
        List<Object[]> results = bookRepository.findDistinctBorrowedBookTitlesWithCount();
        return results.stream()
                .map(result -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("title", result[0]);
                    map.put("borrowedCount", result[1]);
                    return map;
                })
                .collect(Collectors.toList());
    }

}
