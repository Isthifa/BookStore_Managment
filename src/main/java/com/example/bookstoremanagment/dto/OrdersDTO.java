package com.example.bookstoremanagment.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersDTO {
    @NotEmpty
    @Size(min = 5, max = 100, message = "User must be between 5 and 100 characters")
    private String UserName;
    private List<String> booksInCart;
    private Boolean isPaid;
    private Double totalPrice;
}
