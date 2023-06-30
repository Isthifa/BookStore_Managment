package com.example.bookstoremanagment;

import com.example.bookstoremanagment.controller.MainController;
import com.example.bookstoremanagment.entity.UserEntity;
import com.example.bookstoremanagment.service.Services;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
class BookStoreManagmentApplicationTests {

    private static final String ENDPOINT_URL = "/api";
    @InjectMocks
    MainController mainController;

    @MockBean
    Services services;

    @Autowired
    MockMvc mockMvc;

    @Before
    public void setUp()
    {
        this.mockMvc= MockMvcBuilders.standaloneSetup(mainController).build();
    }


    @Test
    public void testSaveUserRegistration() throws Exception {

        UserEntity userEntity=new UserEntity("test","test@gmail.com","1234567894","test@123");
        when(services.save(any())).thenReturn(userEntity);
        //perform the test by making a get call to the endpoint /products/save
        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT_URL + "/save")
                        //set the content type as application/json
                        .contentType("application/json")
                        //set the accept type as application/json
                        .contentType(MediaType.APPLICATION_JSON)
                        //accept the response in json format
                        .accept(MediaType.APPLICATION_JSON))
                //validate the response code received
                .andDo(result -> System.out.println(result.getResponse().getContentAsString()))
                //validate the response body
                .andExpect(status().isOk())
                //validate the response id field exists
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

}

