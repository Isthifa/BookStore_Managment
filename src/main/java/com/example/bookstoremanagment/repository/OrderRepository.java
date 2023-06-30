package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.dto.OrderDetailsDTO;
import com.example.bookstoremanagment.entity.Book;
import com.example.bookstoremanagment.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Orders, UUID> {
    void deleteByUserEntityId(UUID userid);

//    @Query("delete from Orders o where o.userEntity.username=:userName and o.book.title=:bookTitle")
//    @Modifying
    void deleteByUserEntity_UsernameAndBook_Title(String userName,String bookTitle);

    @Query("select o from Orders o where o.userEntity.username=:username")
    Optional<Orders> findByUserEntityName(String username);

    @Query("select o from Orders o where o.userEntity.username=:userName and o.book.title=:bookTitle")
    Optional<Orders> fetchOrderDataByUsernameAndBookTitle(String userName,String bookTitle);

//    @Query("SELECT o.book FROM Orders o GROUP BY o.book ORDER BY SUM(o.quantity) DESC")
//    List<Book> findMostSoldBook();
@Query("SELECT o, u FROM Orders o JOIN o.userEntity u WHERE o.createdAt BETWEEN :startDate AND :endDate")
List<Object[]> findOrdersAndUsersByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


//    @Query("se where e.userEntity.id =:username")
//    Optional<List<Book>> findByUserEntityBook(String username);
}