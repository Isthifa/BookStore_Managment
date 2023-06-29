package com.example.bookstoremanagment.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookDTO {
    @NotBlank(message = "Book name is mandatory")
    @Size(min = 4, max = 50, message = "Book name must be between 3 and 50 characters")
    private String title;
    @Size(min = 4, max = 50, message = "Author name must be between 3 and 50 characters")
    private String authorName;
    @NotNull(message = "Book Price Must be provided")
    private double price;
    @Column(nullable = false)
    private String category;
}
