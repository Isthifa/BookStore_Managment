package com.example.bookstoremanagment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailsDTO {
    private String userName;
    private String authorName;
    private List<String> bookTitle;
    private Double bookPrice;
}
