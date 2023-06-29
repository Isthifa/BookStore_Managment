package com.example.bookstoremanagment;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Book Store",description = "Book Store where author can add books," +
        "user can buy them, admin can delete, search by category name , different operation",version = "V1"))
public class BookStoreManagmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreManagmentApplication.class, args);
    }

}
