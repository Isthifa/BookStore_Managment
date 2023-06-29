package com.example.bookstoremanagment.exception;

import com.example.bookstoremanagment.dto.responseDTO.ApiReponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.io.OutputStream;

@ControllerAdvice
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ApiReponse reponse=new ApiReponse();
        reponse.setStatus(HttpStatus.FORBIDDEN.value());
        reponse.setMessage("Access Denied");
        //OutputStream is used to write data to a destination
        OutputStream out = response.getOutputStream();
        // ObjectMapper is used to convert object to json
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, reponse);
        //out.flush used to
        out.flush();
    }
}
