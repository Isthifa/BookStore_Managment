package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.Book;
import com.example.bookstoremanagment.entity.Cart;
import com.example.bookstoremanagment.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, UUID>{
    Optional<Cart> findByBookAndUserEntity(Book book, UserEntity userEntity);

    @Query("select c from Cart c where c.userEntity.username =:name and c.book.title=:bookTitle")
    Optional<Cart> findByUserEntityAndBook(String name,String bookTitle);
    @Query("SELECT  b from Book b join Cart c on b.id=c.book.id where c.userEntity.username =:name")
    Optional<List<Book>> findByUserEntityCartBook(String name);

    void deleteByUserEntityId(UUID id);

    List<Cart> findByUserEntityId(UUID id);
}
