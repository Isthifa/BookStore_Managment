package com.example.bookstoremanagment.service;

import com.example.bookstoremanagment.dto.*;
import com.example.bookstoremanagment.entity.*;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface Services {

    UserEntity save(UserDTO userDTO);

    Role addRole(RoleDTO roleDTO);

    UserRoleXref saveUserRole(UserRoleDTO userRoleDTO);

    Optional<Role> findByName(Role.RoleType role);

    Book saveBook(BookDTO bookDTO);

    Categories saveCategories(CategoriesDTO categoriesDTO);

    Iterable<BookDetailsDTO> findByCategories(String catType);

    CartDTO saveToCart(CartDTO cartDTO);

    Orders saveOrder(String userName,String boolTitle,boolean ispaid);

//    OrderDetailsDTO fetchOrderDetails(String username);
    void deleteByUserName(String name);

}
