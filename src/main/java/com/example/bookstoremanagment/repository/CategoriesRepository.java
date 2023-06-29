package com.example.bookstoremanagment.repository;

import com.example.bookstoremanagment.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoriesRepository extends JpaRepository<Categories, UUID> {
    Optional<Categories> findByCatName(String catName);
}
