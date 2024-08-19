package com.example.library.dto;

import com.example.library.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    private LocalDate membershipDate;

    private List<BookDTO> books;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.name = member.getName();
        this.membershipDate = member.getMembershipDate();
        this.books = member.getBooks().stream()
                .map(BookDTO::new)
                .collect(Collectors.toList());
    }

}
