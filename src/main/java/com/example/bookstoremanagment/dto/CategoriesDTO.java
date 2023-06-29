package com.example.bookstoremanagment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoriesDTO {
    @NotEmpty
    @Size(min = 4, max = 20, message = "Category name must be between 3 and 20 characters")
    private String catName;
}
