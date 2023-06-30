package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.Book;
import com.example.bookstoremanagment.entity.BookCategoriesXref;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BookCategoriesXrefRepository extends JpaRepository<BookCategoriesXref, UUID> {

    @Query("select b.book from BookCategoriesXref  b where b.categories.catName=:catType")
    List<Book> findByCategoriesCatName(String catType);

    void deleteBy();

    void deleteByCategoriesId(UUID id);

    void deleteByBook_Title(String title);
}
