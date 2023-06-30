package com.example.bookstoremanagment.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Book extends BaseModel implements Serializable {

    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String authorName;
    @Column(nullable = false)
    private double price;
}
