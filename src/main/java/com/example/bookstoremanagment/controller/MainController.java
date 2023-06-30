package com.example.bookstoremanagment.controller;

import com.example.bookstoremanagment.dto.*;
import com.example.bookstoremanagment.dto.responseDTO.CatReponse;
import com.example.bookstoremanagment.dto.responseDTO.CommonResponse;
import com.example.bookstoremanagment.dto.responseDTO.TokenReponse;
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
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDate;
import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class MainController {
    @Autowired
    private final Services services;
    private final CartRepository cartRepository;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    //User Account Creation and Role Creation
    @Operation(summary = "user registration(before user registration must add roles")
    @ApiResponse(responseCode = "200", description = "user registered successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PostMapping("/save")
    public ResponseEntity<UserEntity> save(@RequestBody @Valid UserDTO userDTO) {
        UserEntity userEntity = services.save(userDTO);
        log.info("Saving the user data registration");
        return ResponseEntity.ok(userEntity);
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
    }

    //User search book by category name
    @Operation(summary = "Search book By category name")
    @ApiResponse(responseCode = "200", description = "Book data found")
    @ApiResponse(responseCode = "404", description = "book not found")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/byCategory")
    public CatReponse<Iterable<BookDetailsDTO>> books(@RequestParam("cateType") String cateType)
//                                                      @RequestParam("pageNumber") int pageNumber,
//                                                      @RequestParam("pageSize") int pageSize)
    {
        Iterable<BookDetailsDTO> book = services.findByCategories(cateType);
        log.info("find book by category name");
        return new CatReponse<>(cateType, book);
    }

    @Operation(summary = "Order book which is in cart added by user ")
    @ApiResponse(responseCode = "200", description = "Order successful")
    @ApiResponse(responseCode = "404", description = "Book Not found")
    @PreAuthorize("hasAnyRole('USER','AUTHOR')")
    @PostMapping("/Order")
    public ResponseEntity<OrdersDTO> orderBook(@RequestParam("userName") String userName,
                                               @RequestParam("isPaid") boolean ispaid,
                                               @RequestParam("bookTitle") String bookTitle) {
        Optional<List<Book>> books = cartRepository.findByUserEntityCartBook(userName);
        Optional<Cart> cart = cartRepository.findByUserEntityAndBook(userName, bookTitle);
        List<String> booksInCart = new ArrayList<>();
        double totalPrice = 0;
        if (cart.isPresent()) {
            for (Book book : books.get()) {
                totalPrice += book.getPrice() * cart.get().getQuantity();
                booksInCart.add(book.getTitle());
            }
        }
        log.info("saving order data");
        services.saveOrder(userName, bookTitle, ispaid);
        OrdersDTO ordersDTO = OrdersDTO.builder()
                .UserName(userName)
                .isPaid(ispaid)
                .booksInCart(booksInCart)
                .totalPrice(totalPrice).build();
        return ResponseEntity.ok(ordersDTO);
    }

//    @PreAuthorize("hasRole('USER')")
//    @GetMapping("/orderdetails")
//    public ResponseEntity<OrderDetailsDTO> getorderDetails(@RequestParam("username")String username)
//    {
//        return new ResponseEntity<>(services.fetchOrderDetails(username), HttpStatus.ACCEPTED);
//    }

    @Operation(summary = "Add Books to cart")
    @ApiResponse(responseCode = "200", description = "Added successfully")
    @ApiResponse(responseCode = "404", description = "Book Not found")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addtoCart")
    public ResponseEntity<CartDTO> addToCart(@RequestBody @Valid CartDTO cartDTO) {
        log.info("adding book to cart");
        services.saveToCart(cartDTO);
        return ResponseEntity.ok(cartDTO);
    }

    @Operation(summary = "Save the book details. added by author")
    @ApiResponse(responseCode = "200", description = "Saved successfully")
    @ApiResponse(responseCode = "404", description = "Category Not found")
    //Author can add books
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/addBook")
    public ResponseEntity<Book> saveBook(@RequestBody @Valid BookDTO bookDTO) {
        log.info("adding the books");
        Book book1 = services.saveBook(bookDTO);
        return ResponseEntity.ok(book1);
    }

    @Operation(summary = "Add category")
    @ApiResponse(responseCode = "200", description = "added successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/addCategory")
    public ResponseEntity<Categories> addCategory(@RequestBody @Valid CategoriesDTO categoriesDTO) {
        log.info("adding category");
        Categories categories = services.saveCategories(categoriesDTO);
        return ResponseEntity.ok(categories);
    }

//    @GetMapping("/test")
//    public String roel()
//    {
//        return "test";
//    }

    @Operation(summary = "Add Roles to db")
    @ApiResponse(responseCode = "200", description = "Added Successfully")
    @ApiResponse(responseCode = "404", description = "Role not found")
    @PostMapping("/AddRoles")
    public ResponseEntity<Role> addRoles(@RequestBody @Valid RoleDTO RoleDTO) {
        log.info("adding roles");
        Role role = services.addRole(RoleDTO);
        return ResponseEntity.ok(role);
    }

    @Operation(summary = "Jwt Authentication to access book store token must")
    @ApiResponse(responseCode = "200", description = "Token generated")
    @ApiResponse(responseCode = "404", description = "User name and password not valid")
    @PostMapping("/authenticate")
    public ResponseEntity<TokenReponse> authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        log.info("user authentication");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.genrateToken(authentication);
            return new ResponseEntity<>(new TokenReponse(accessToken, new Date()), HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid user request");
        }
    }

    @Operation(summary = "Delete the user by admin")
    @ApiResponse(responseCode = "200", description = "user deleted")
    @ApiResponse(responseCode = "404", description = "user not found")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/delete/user")
    public ResponseEntity<CommonResponse> deleteByname(@RequestParam("username") String name) {
        log.info("delete enter user data by admin");
        services.deleteByUserName(name);
        return new ResponseEntity<>(new CommonResponse("User Deleted"), HttpStatus.OK);
    }

    @Operation(summary = "Update the category name of book")
    @ApiResponse(responseCode = "200", description = "updated successfully")
    @ApiResponse(responseCode = "404", description = "Category name not found")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @PutMapping("/data/update")
    public ResponseEntity<String> update(@RequestParam("cname") String cname, @RequestParam("newcatname") String catName) {
        log.info("Category data update");
        services.updateCategory(cname, catName);
        return ResponseEntity.ok("Category updated =" + catName);
    }

    @Operation(summary = "cancel the order")
    @ApiResponse(responseCode = "200", description = "cancel successfully")
    @ApiResponse(responseCode = "404", description = "order not found")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/user/order/cancel")
    public ResponseEntity<String> cancelOrder(@RequestParam("userName") String userName, @RequestParam("bookTitle") String bookTitle) {
        services.CancelTheOrder(userName, bookTitle);
        return ResponseEntity.ok("User Canceled the order");
    }

    @Operation(summary = "user cart delete")
    @ApiResponse(responseCode = "200", description = "updated successfully")
    @ApiResponse(responseCode = "404", description = "not found")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/user/cart/delete")
    public ResponseEntity<String> userCartDelete(@RequestParam("userName") String userName,
                                                 @RequestParam("bookTitle") String bookTitle) {
        services.deleteCartDataByUserAndBookName(userName,bookTitle);
        return ResponseEntity.ok("Cart deleted by user");
    }

    @Operation(summary = "Update the cart book book quantity by user")
    @ApiResponse(responseCode = "200", description = "updated successfully")
    @ApiResponse(responseCode = "404", description = "Category name not found")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/user/cart/quantity")
    public ResponseEntity<String> userCartQunatity(@RequestParam("userName") String userName,
                                                 @RequestParam("bookTitle") String bookTitle,
                                                 @RequestParam("quantity") long quantity) {
        services.CartQuantityUpdateByUser(userName, bookTitle,quantity);
        return ResponseEntity.ok("Cart quantity updated "+quantity);
    }

    @Operation(summary = "delete book")
    @ApiResponse(responseCode = "200", description = "deleted successfully")
    @ApiResponse(responseCode = "404", description = " not found")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @DeleteMapping("/admin/delete/book")
    public ResponseEntity<String> deleteBook(@RequestParam("bookTitle") String bookTitle) {
        services.deleteBookByTitle(bookTitle);
        return ResponseEntity.ok("Book deleted");
    }

    @Operation(summary = "Most sold books")
    @ApiResponse(responseCode = "200", description = "successfully")
    @ApiResponse(responseCode = "404", description = "not found")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @GetMapping("/book/most/sold")
    public ResponseEntity<List<MostSellDTO>> mostsoldbook() {
        return ResponseEntity.ok(services.mostSellBook());
    }

    @Operation(summary = "Order info by date range")
    @ApiResponse(responseCode = "200", description = "order info by date range")
    @ApiResponse(responseCode = "404", description = " not found")
    @PreAuthorize("hasAnyRole('ADMIN','AUTHOR')")
    @GetMapping("/book/order/info")
    public ResponseEntity<Map<String,String>> orderInfo(@RequestParam("startDate") LocalDate startDate,
                                                        @RequestParam("endDate") LocalDate endDate){
        Map<String,String> map=services.BookByDateRange(startDate,endDate);
        return ResponseEntity.ok(map);
    }

}
