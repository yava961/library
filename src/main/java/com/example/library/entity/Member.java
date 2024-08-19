package com.example.library.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Member extends EntityBase {

    private String name;
    private LocalDate membershipDate;

    @OneToMany
    private List<Book> books = new ArrayList<>();
}
