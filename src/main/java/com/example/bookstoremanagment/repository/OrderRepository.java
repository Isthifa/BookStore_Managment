package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.dto.OrderDetailsDTO;
import com.example.bookstoremanagment.entity.Book;
import com.example.bookstoremanagment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    void deleteByUserEntityId(UUID userid);

//    @Query("se where e.userEntity.id =:username")
//    Optional<List<Book>> findByUserEntityBook(String username);
}