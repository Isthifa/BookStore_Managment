package com.example.bookstoremanagment.service;

import com.example.bookstoremanagment.dto.*;
import com.example.bookstoremanagment.entity.*;
import com.example.bookstoremanagment.exception.BookBadRequestException;
import com.example.bookstoremanagment.exception.BookNotFoundException;
import com.example.bookstoremanagment.exception.UserNotFound;
import com.example.bookstoremanagment.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class ServicesImpl implements Services {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final BookRepository bookRepository;
    private final CategoriesRepository categoriesRepository;
    private final BookCategoriesXrefRepository bookCategoriesXrefRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity userEntity = modelMapper.map(userDTO, UserEntity.class);
        Role.RoleType role = userDTO.getRoleName();
        Optional<Role> role1 = roleRepository.findByRoleName(role);
        if (!role1.isPresent()) {
            throw new RuntimeException("Role not found");
        } else {
            UserRoleDTO userRoleDTO = UserRoleDTO.builder()
                    .role(role1.get())
                    .userEntity(userEntity).build();
            UserRoleXref userRoleXref = modelMapper.map(userRoleDTO, UserRoleXref.class);
            userRoleRepository.save(userRoleXref);
        }
        //saving the roles while registering
//        UserRoleDTO userRoleDTO=new UserRoleDTO();
//        Role.RoleType role=userDTO.getRoleName();
//        Role role1=roleRepository.findByName(role);
//        if(role1==null)
//        {
//            throw new RuntimeException("Role not found");
//        }else {
//            userRoleDTO.setRole(role1);
//            userRoleDTO.setUserEntity(userEntity);
//            UserRole userRole=modelMapper.map(userRoleDTO,UserRole.class);
//            userRoleRepository.save(userRole);
//        }
        return userRepository.save(userEntity);
    }

    @Override
    public Role addRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);
        return roleRepository.save(role);
    }

    @Override
    public UserRoleXref saveUserRole(UserRoleDTO userRoleDTO) {
        UserRoleXref userRoleXref = modelMapper.map(userRoleDTO, UserRoleXref.class);
        return userRoleRepository.save(userRoleXref);
    }

    @Override
    public Optional<Role> findByName(Role.RoleType role) {
        return roleRepository.findByRoleName(role);
    }

    @Override
    public Book saveBook(BookDTO bookDTO) {
        String author = bookDTO.getAuthorName();
        String catName = bookDTO.getCategory();
        Optional<UserEntity> userEntity = userRepository.findByUsername(author);
        Optional<Categories> categories = categoriesRepository.findByCatName(catName);
        if (!categories.isPresent()) {
            throw new BookNotFoundException("Category not found");
        }
        categoriesRepository.save(categories.get());
        //author role check while adding book
        Optional<UserRoleXref> userRoleXref =
                userRoleRepository.findByUserEntityAndRole(userEntity.get().getId(), Role.RoleType.ROLE_AUTHOR);
        System.out.println(userRoleXref);
        Book book = modelMapper.map(bookDTO, Book.class);
        if (userRoleXref.isPresent()) {
            BookCategoriesXref bookCategoriesXref = BookCategoriesXref.builder()
                    .categories(categories.get())
                    .book(book).build();
            bookCategoriesXrefRepository.save(bookCategoriesXref);
            return bookRepository.save(book);
        } else {
            throw new RuntimeException("Author not found");
        }

    }

    @Override
    public Categories saveCategories(CategoriesDTO categoriesDTO) {
        Categories categories = modelMapper.map(categoriesDTO, Categories.class);
        return categoriesRepository.save(categories);
    }

    @Override
    public Iterable<BookDetailsDTO> findByCategories(String catType) {
//        Pageable pageable = PageRequest.of(pageNumber,pageSize);
//                bookCategoriesXrefRepository.findByCategoriesCatName(catType,pageable);
//        Page<Book> bookpage=bookCategoriesXrefRepository.findByCategoriesCatName(catType,pageable);
//        BookDetailsDTO bookDetailsDTO=modelMapper.map(bookpage,BookDetailsDTO.class);
        List<Book> books = bookCategoriesXrefRepository.findByCategoriesCatName(catType);
        List<BookDetailsDTO> bookDetailsDTOS = new ArrayList<>();
        for (Book book1 : books) {
            BookDetailsDTO bookDetailsDTO = modelMapper.map(book1, BookDetailsDTO.class);
            System.out.println(bookDetailsDTO);
            bookDetailsDTOS.add(bookDetailsDTO);
        }
        return bookDetailsDTOS;
    }

    @Override
    public CartDTO saveToCart(CartDTO cartDTO) {
        Optional<Book> book = bookRepository.findByTitle(cartDTO.getBookName());
        Optional<UserEntity> userEntity = userRepository.findByUsername(cartDTO.getUserName());
        if (!book.isPresent()) {
            throw new BookNotFoundException("Book not found");
        }
        if (!userEntity.isPresent()) {
            throw new UserNotFound("User not found");
        }
        Optional<Cart> carte = cartRepository.findByBookAndUserEntity(book.get(), userEntity.get());
        if (carte.isPresent()) {
            throw new BookBadRequestException("Book already in cart");
        } else {
            Cart cart = Cart.builder()
                    .book(book.get())
                    .userEntity(userEntity.get())
                    .quantity(cartDTO.getQuantity())
                    .build();
            cartRepository.save(cart);
        }
        return cartDTO;
    }

    @Override
    public Orders saveOrder(String userName, String bookTitle, boolean ispaid) {
        Optional<Cart> cart = cartRepository.findByUserEntityAndBook(userName, bookTitle);
//        Optional<List<Book>> books = cartRepository.findByUserEntityCartBook(userName);
        Optional<Book> book=cartRepository.findByUserEntityNameAndBookName(userName,bookTitle);
        Optional<Orders> orderss = orderRepository.fetchOrderDataByUsernameAndBookTitle(userName, bookTitle);
        if (orderss.isPresent()) {
            throw new BookBadRequestException("Book already in orders");
        }
                if (!cart.isPresent() && !orderss.isPresent()) {
                    throw new BookNotFoundException("Book not found in Cart");
                }else {
                        Orders orders = new Orders();
                        orders.setBook(book.get());
                        orders.setUserEntity(cart.get().getUserEntity());
                        orders.setPaid(ispaid);
                        orders.setQuantity(cart.get().getQuantity());
                        orderRepository.save(orders);
                        return orders;
                    }
        }

    @Override
    public void deleteByUserName(String name) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(name);
        if (userEntity.isPresent()) {
            userRepository.deleteByUsername(userEntity.get().getUsername());
            userRoleRepository.deleteByUserEntityId(userEntity.get().getId());
        }
    }

    @Override
    public void updateCategory(String cname, String catName) {
        Optional<Categories> categories = categoriesRepository.findByCatName(cname);
        if (categories.isPresent()) {
            categories.get().setCatName(catName);
            categoriesRepository.save(categories.get());
        } else {
            throw new RuntimeException("Category not found");
        }
    }

    @Override
    public void CancelTheOrder(String userName,String bookTitle) {
 Optional<Orders> orders = orderRepository.findByUserEntityName(userName);
        System.out.println(orders.get());
        if (orders.isPresent()) {
            orderRepository.deleteByUserEntity_UsernameAndBook_Title(userName,bookTitle);
        } else {
            throw new BookNotFoundException("Order not found");
        }
    }

    @Override
    public void deleteCartDataByUserAndBookName(String userName, String bookTitle) {
        Optional<Cart> cart = cartRepository.findByUserEntityAndBook(userName, bookTitle);
        Optional<Orders> orders=orderRepository.fetchOrderDataByUsernameAndBookTitle(userName,bookTitle);
        if (cart.isPresent() || cart.isPresent()&&orders.isPresent()) {
            orderRepository.deleteByUserEntity_UsernameAndBook_Title(userName,bookTitle);
            cartRepository.deleteByUserEntity_UsernameAndBook_Title(userName,bookTitle);
        } else {
            throw new BookNotFoundException("Book not found in Cart");
        }
    }

    @Override
    public void CartQuantityUpdateByUser(String userName, String bookTitle, long quantity) {
        Optional<Cart> cart=cartRepository.findByUserEntityAndBook(userName,bookTitle);
        if (cart.isPresent()) {
            cart.get().setQuantity(quantity);
            cartRepository.save(cart.get());
        } else {
            throw new BookNotFoundException("Book not found in Cart");
        }
    }

    @Override
    public void deleteBookByTitle(String title) {
        Optional<Book> book=bookRepository.findByTitle(title);
        if (book.isPresent()) {
            bookRepository.delete(book.get());
//            bookCategoriesXrefRepository.deleteByBook_Title(title);
//            cartRepository.deleteByBook_Title(title);
        } else {
            throw new BookNotFoundException("Book not found");
        }
    }

    @Override
    public List<MostSellDTO> mostSellBook() {
        List<Book> books=cartRepository.findMostSoldBook();
        List<MostSellDTO> mostSellDTOS=new ArrayList<>();
        for(Book book:books) {
            MostSellDTO mostSellDTO = new MostSellDTO();
            mostSellDTO.setBookTitle(book.getTitle());
            mostSellDTOS.add(mostSellDTO);
        }
        return mostSellDTOS;
    }

    @Override
    public Map<String, String> BookByDateRange(LocalDate startDate,LocalDate endDate) {
        Map<String,String> map=new HashMap<>();
        List<Object[]> orders=orderRepository.findOrdersAndUsersByDateRange(startDate,endDate);
        for(Object[] order:orders) {
            map.put("Output","Book Title: " + order[0] + " User Name: " + order[1] + " Order Date: " + order[2]);
        }
        return map;
    }
}


//    //Admin Delete all user
//    @Override
//    public void deleteByUserName(String name) {
//        Optional<UserEntity> userEntity = userRepository.findByUsername(name);
//        if (userEntity.isPresent()) {
//            List<Cart> cart=cartRepository.findByUserEntityId(userEntity.get().getId());
//            Optional<Book> book=bookRepository.findByTitle(cart.get(0).getBook().getTitle());
//            Optional<Book> book1=bookRepository.findByTitle(cart.get(0).getBook().getTitle());
//            userRepository.deleteByUsername(name);
//            cartRepository.deleteByUserEntityId(userEntity.get().getId());
//            orderRepository.deleteByUserEntityId(userEntity.get().getId());
//            userRoleRepository.deleteByUserEntityId(userEntity.get().getId());
//            bookCategoriesXrefRepository.deleteByCategoriesId(cart.get(0).getId());
//            bookCategoriesXrefRepository.deleteByCategoriesId(cart.get(1).getId());
//            bookRepository.deleteById(book.get().getId());
//            bookRepository.deleteById(book1.get().getId());
//        }}


    //public OrderDetailsDTO fetchOrderDetails(String username) {
////        Optional<List<Book>> books=orderRepository.findByUserEntityBook(username);
////        List<String> bookTitle=new ArrayList<>();
////        for(Book book:books.get())
////        {
////            bookTitle.add(book.getTitle());
////        }
////        OrderDetailsDTO orderDetailsDTO=new OrderDetailsDTO();
////        orderDetailsDTO.setAuthorName(books.get().get(0).getAuthorName());
////        orderDetailsDTO.setBookTitle(bookTitle);
////        orderDetailsDTO.setBookPrice(books.get().get(0).getPrice());
////        return orderDetailsDTO;
//    }
