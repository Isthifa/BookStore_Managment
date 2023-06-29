package com.example.bookstoremanagment.exception;

import com.example.bookstoremanagment.dto.responseDTO.ApiReponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.io.IOException;
import java.io.OutputStream;

@ControllerAdvice
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ApiReponse apiReponse=new ApiReponse();
        apiReponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        apiReponse.setMessage("Unauthorized");
        OutputStream out = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, apiReponse);
        out.flush();
    }
}