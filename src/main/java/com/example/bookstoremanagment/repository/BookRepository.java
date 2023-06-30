package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface BookRepository extends JpaRepository<Book, UUID>{
    Optional<Book> findByTitle(String bookName);

    void deleteByTitle(String title);
}
