package com.example.library;

import com.example.library.dto.MemberDTO;
import com.example.library.entity.Book;
import com.example.library.entity.Member;
import com.example.library.exveption.BookOutOfStockException;
import com.example.library.exveption.MemberCannotBeDeletedException;
import com.example.library.repository.BookRepository;
import com.example.library.repository.MemberRepository;
import com.example.library.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BookRepository bookRepository;

    private Member member;
    private Book book;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        member = new Member();
        member.setId(1L);
        member.setName("John Doe");
        member.setMembershipDate(LocalDate.now());

        book = new Book();
        book.setId(1L);
        book.setTitle("Test Title");
        book.setAuthor("Test Author");
        book.setAmount(10);
    }

    @Test
    public void testSaveMember() {
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setName("John Doe");

        MemberDTO savedMemberDTO = memberService.saveMember(memberDTO);

        assertEquals(1L, savedMemberDTO.getId());
        assertEquals("John Doe", savedMemberDTO.getName());
    }

    @Test
    public void testGetMemberById() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        MemberDTO memberDTO = memberService.getMemberById(1L);

        assertEquals(1L, memberDTO.getId());
        assertEquals("John Doe", memberDTO.getName());
    }

    @Test
    public void testGetAllMembers() {
        when(memberRepository.findAll()).thenReturn(List.of(member));

        List<MemberDTO> members = memberService.getAllMembers();

        assertNotNull(members);
        assertEquals(1, members.size());
        assertEquals("John Doe", members.get(0).getName());
    }

    @Test
    public void testDeleteMember() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

        // No exception should be thrown
        assertDoesNotThrow(() -> memberService.deleteMember(1L));
    }

    @Test
    public void testDeleteMemberCannotBeDeleted() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        member.setBooks(List.of(book)); // Simulate that the member has borrowed books

        Exception exception = assertThrows(MemberCannotBeDeletedException.class, () -> memberService.deleteMember(1L));
        assertEquals("Member cannot be deleted because they have borrowed books, id: 1", exception.getMessage());
    }

    @Test
    public void testBorrowBook() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        // Decrease amount of book
        book.setAmount(10);

        assertDoesNotThrow(() -> memberService.borrowBook(1L, 1L));
        assertEquals(9, book.getAmount());
    }

    @Test
    public void testBorrowBookOutOfStock() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        book.setAmount(0);
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        Exception exception = assertThrows(BookOutOfStockException.class, () -> memberService.borrowBook(1L, 1L));
        assertEquals("Book with id 1 is out of stock.", exception.getMessage());
    }

    @Test
    public void testReturnBook() {
        when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));

        book.setAmount(9);
        member.setBooks(List.of(book));

        assertDoesNotThrow(() -> memberService.returnBook(1L, 1L));
        assertEquals(10, book.getAmount());
    }
}
