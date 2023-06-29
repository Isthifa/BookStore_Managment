package com.example.bookstoremanagment.controller;

import com.example.bookstoremanagment.dto.*;
import com.example.bookstoremanagment.dto.responseDTO.CatReponse;
import com.example.bookstoremanagment.dto.responseDTO.CommonResponse;
import com.example.bookstoremanagment.entity.*;
import com.example.bookstoremanagment.repository.CartRepository;
import com.example.bookstoremanagment.repository.OrderRepository;
import com.example.bookstoremanagment.service.JwtService;
import com.example.bookstoremanagment.service.Services;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final Services services;
    private final CartRepository cartRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //User Account Creation and Role Creation
    @Operation(summary = "user registration(before user registration must add roles")
    @ApiResponse(responseCode = "200",description = "user registered successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PostMapping("/save")
    public ResponseEntity<UserEntity> save(@RequestBody @Valid UserDTO userDTO)
    {
        UserEntity userEntity = services.save(userDTO);
//        Role.RoleType role=userDTO.getRoleName();
//        OptionalRole role1=services.findByName(role);
//        if(role1==null) {
//            throw new RuntimeException("Role not found");
//        }else{
//            UserRoleDTO userRoleDTO= UserRoleDTO.builder()
//                    .role(role1)
//                    .userEntity(userEntity).build();
//            services.saveUserRole(userRoleDTO);
//        }
        log.info("Saving the user data registration");
        return ResponseEntity.ok(userEntity);
    }

    //User search book by category name
    @Operation(summary = "Search book By category name")
    @ApiResponse(responseCode = "200",description = "Book data found")
    @ApiResponse(responseCode = "404", description = "book not found")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/byCategory")
    public CatReponse<Iterable<BookDetailsDTO>> books(@RequestParam("cateType") String cateType)
//                                                      @RequestParam("pageNumber") int pageNumber,
//                                                      @RequestParam("pageSize") int pageSize)
    {
        Iterable<BookDetailsDTO> book=services.findByCategories(cateType);
        log.info("find book by category name");
        return new CatReponse<>(cateType,book);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/Order")
    public ResponseEntity<OrdersDTO> orderBook(@RequestParam("userName") String userName,
                                               @RequestParam("isPaid") boolean ispaid,
                                               @RequestParam("bookTitle") String bookTitle){
        Optional<List<Book>> books=cartRepository.findByUserEntityCartBook(userName);
        double totalPrice = 0;
        for(Book book:books.get())
        {
            totalPrice+=book.getPrice();
        }
        log.info("saving the user data");
        services.saveOrder(userName,bookTitle,ispaid);
        OrdersDTO ordersDTO= OrdersDTO.builder()
                .UserName(userName)
                .isPaid(ispaid)
                .totalPrice(totalPrice).build();
        return ResponseEntity.ok(ordersDTO);
    }

//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/orderdetails")
//    public ResponseEntity<OrderDetailsDTO> getorderDetails(@RequestParam("username")String username)
//    {
//        return new ResponseEntity<>(services.fetchOrderDetails(username), HttpStatus.ACCEPTED);
//    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addtoCart")
    public ResponseEntity<CartDTO> addToCart(@RequestBody @Valid CartDTO cartDTO)
    {
        log.info("adding book to cart");
        services.saveToCart(cartDTO);
        return ResponseEntity.ok(cartDTO);
    }


    //Author can add books
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/addBook")
    public ResponseEntity<Book> saveBook(@RequestBody @Valid BookDTO bookDTO)
    {
        log.info("adding the books");
        Book book1=services.saveBook(bookDTO);
        return ResponseEntity.ok(book1);
    }

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/addCategory")
    public ResponseEntity<Categories> addCategory(@RequestBody @Valid CategoriesDTO categoriesDTO)
    {
        log.info("adding category");
        Categories categories=services.saveCategories(categoriesDTO);
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/test")
    public String roel()
    {
        return "test";
    }

    @PostMapping("/AddRoles")
    public ResponseEntity<Role> addRoles(@RequestBody @Valid RoleDTO RoleDTO)
    {
        log.info("adding roles");
        Role role=services.addRole(RoleDTO);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info("user authentication");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken=jwtService.genrateToken(authentication);
            return ResponseEntity.ok(accessToken);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/user")
    public ResponseEntity<CommonResponse> deleteByname(@RequestParam("username")String name)
    {
        log.info("delete enter user data by admin");
        services.deleteByUserName(name);
        return new ResponseEntity<>(new CommonResponse("User Deleted"),HttpStatus.OK);
    }
}
