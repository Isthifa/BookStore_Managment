package com.example.bookstoremanagment;

import com.example.bookstoremanagment.controller.MainController;
import com.example.bookstoremanagment.dto.BookDetailsDTO;
import com.example.bookstoremanagment.dto.CartDTO;
import com.example.bookstoremanagment.dto.UserDTO;
import com.example.bookstoremanagment.dto.responseDTO.CatReponse;
import com.example.bookstoremanagment.entity.Role;
import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.service.Services;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.example.bookstoremanagment.entity.Role.RoleType.ROLE_AUTHOR;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class UserRegistrationTest {
    private static final String ENDPOINT_URL = "/api";
    @InjectMocks
    MainController mainController;

    @MockBean
    @Autowired
    Services services;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(mainController).build();
    }


//    public void testSaveUserRegistrationDate() throws Exception {
//
//        UserDTO us=new UserDTO("test","test@gmail.com","1234567894","test@123",ROLE_AUTHOR);
//        when(services.save(any())).thenReturn(userDTO.getPassword());
//        //perform the test by making a get call to the endpoint /products/save
//        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/save")
//                        //set the content type as application/json
//                        .contentType("application/json")
//                        //set the accept type as application/json
//                        .contentType(MediaType.APPLICATION_JSON)
//                        //accept the response in json format
//                        .accept(MediaType.APPLICATION_JSON))
//                //validate the response code received
//                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
//                //validate the response body
//                .andExpect(status().isOk())
//                //validate the response id field exists
//                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
//    }
    @Test
    public void testSaveUser() throws Exception {
        // Prepare test data
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPhone("1234567890");
        userDTO.setPassword("Password@123");
        userDTO.setRoleName(Role.RoleType.ROLE_USER);

        // Perform the request and receive the response
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDTO)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        // Assert any other expectations on the response if needed
        // ...

    }
    @Test
    public void testBooks() {
        // Arrange
        String cateType = "yourCategoryType";
        List<BookDetailsDTO> bookList = new ArrayList<>();
        // Add some BookDetailsDTO objects to the bookList

        when(services.findByCategories(cateType)).thenReturn(bookList);

        // Act
        Iterable<BookDetailsDTO> response = services.findByCategories(cateType);

        // Assert
        assertEquals(cateType, response.iterator().next().getBookTitle());
        assertEquals(bookList, response.iterator().next());
        // Add more assertions as needed
    }
    @Test
    public void testAddToCart() {
        // Arrange
        CartDTO cartDTO = new CartDTO(); // Create a CartDTO object for testing
        // Set properties on the cartDTO as needed

        when(services.saveToCart(cartDTO)).thenReturn(cartDTO);

        // Act
        CartDTO response = services.saveToCart(cartDTO);

        // Assert
        assertEquals(cartDTO,response);
        verify(services).saveToCart(cartDTO);
    }

    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

