package com.example.bookstoremanagment.dto;

import com.example.bookstoremanagment.entity.Book;
import com.example.bookstoremanagment.entity.Categories;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookCatergoriesXrefDTO {
    @Column(nullable = false)
    private Book book;
    @Column(nullable = false)
    private Categories categories;
}
