package com.example.library.dto;

import com.example.library.entity.Book;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private Long id;

    @NotBlank(message = "Title is required")
    @Pattern(regexp = "^[A-Z].*", message = "Title should start with a capital letter")
    @Min(value = 3, message = "Title should have at least 3 characters")
    private String title;

    @NotBlank(message = "Author is required")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+$", message = "Author should contain two capital words with name and surname")
    private String author;
    @Min(value = 1, message = "Amount should be at least 1")
    private int amount;

    private Long memberId;

    public BookDTO(Book book) {
        this.id = book.getId();
        this.title = book.getTitle();
        this.author = book.getAuthor();
        this.amount = book.getAmount();
        this.memberId = book.getMember() != null ? book.getMember().getId() : null;
    }
}
