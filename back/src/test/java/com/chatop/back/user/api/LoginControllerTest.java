package com.chatop.back.user.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.user.api.controller.LoginController;
import com.chatop.back.user.application.command.LoginUserCommand;
import com.chatop.back.user.application.usecase.LoginUserUseCase;
import com.chatop.back.user.domain.exception.InvalidCredentialsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class LoginControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean LoginUserUseCase loginUserUseCase;

    @Test
    void returns_200_with_token_on_valid_credentials() throws Exception {
        when(loginUserUseCase.handle(any(LoginUserCommand.class))).thenReturn("jwt-token");

        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void returns_400_on_missing_password() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com"
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void returns_401_on_bad_credentials() throws Exception {
        when(loginUserUseCase.handle(any(LoginUserCommand.class)))
                .thenThrow(new InvalidCredentialsException());

        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com",
                "password", "wrong"
        ));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("error"));
    }
}
