package com.example.library;

import com.example.library.dto.BookDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Member;
import com.example.library.exveption.BookCannotBeDeletedException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import com.example.library.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;

    @MockBean
    private BookRepository bookRepository;

    @MockBean
    private MemberRepository memberRepository;

    private Book book;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setAmount(10);
    }

    @Test
    public void testSaveBook() {
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setTitle("Test Title");
        bookDTO.setAuthor("Test Author");
        bookDTO.setAmount(10);

        BookDTO savedBookDTO = bookService.saveBook(bookDTO);

        assertEquals(1L, savedBookDTO.getId());
        assertEquals("Test Title", savedBookDTO.getTitle());
        assertEquals("Test Author", savedBookDTO.getAuthor());
        assertEquals(10, savedBookDTO.getAmount());
    }

    @Test
    public void testGetBookById() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        BookDTO bookDTO = bookService.getBookById(1L);

        assertEquals(1L, bookDTO.getId());
        assertEquals("Test Title", bookDTO.getTitle());
        assertEquals("Test Author", bookDTO.getAuthor());
        assertEquals(10, bookDTO.getAmount());
    }

    @Test
    public void testGetAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));

        List<BookDTO> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals("Test Title", books.get(0).getTitle());
    }

    @Test
    public void testDeleteBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        assertDoesNotThrow(() -> bookService.deleteBook(1L));
    }

    @Test
    public void testDeleteBookCannotBeDeleted() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        book.setMember(new Member()); // Simulate a borrowed book

        Exception exception = assertThrows(BookCannotBeDeletedException.class, () -> bookService.deleteBook(1L));
        assertEquals("Book cannot be deleted because it is borrowed, id: 1", exception.getMessage());
    }
}
