package com.example.bookstoremanagment.dto;


import com.example.bookstoremanagment.entity.Book;
import com.example.bookstoremanagment.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
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
public class CartDTO {
    @NotEmpty(message = "Book name should not be empty")
    @Column(nullable = false)
    private String bookName;
    @NotEmpty(message = "User name should not be empty")
    @Column(nullable = false)
    private String userName;
    @Min(value = 1,message = "Quantity should be greater than 0")
    private long quantity;
}
