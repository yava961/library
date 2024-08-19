package com.example.library.web;

import com.example.library.dto.BookDTO;
import com.example.library.dto.MemberDTO;
import com.example.library.service.MemberService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/members")
public class MemberController {
    @Autowired
    private MemberService memberService;

    @PostMapping
    public ResponseEntity<MemberDTO> addMember(@RequestBody @Valid MemberDTO memberDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.saveMember(memberDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDTO> getMember(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getMemberById(id));
    }

    @GetMapping
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        return ResponseEntity.ok(memberService.getAllMembers());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        memberService.deleteMember(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{memberId}/borrow/{bookId}")
    public ResponseEntity<Void> borrowBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        memberService.borrowBook(memberId, bookId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{memberId}/return/{bookId}")
    public ResponseEntity<Void> returnBook(@PathVariable Long memberId, @PathVariable Long bookId) {
        memberService.returnBook(memberId, bookId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{name}/books")
    public ResponseEntity<List<BookDTO>> findBorrowedBooksByMemberName(@PathVariable String name) {
        List<BookDTO> books = memberService.findBorrowedBooksByMemberName(name);
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(books);
    }
}
