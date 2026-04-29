package com.chatop.back.auth.controller;

import com.chatop.back.auth.service.AuthService;
import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.user.exception.EmailAlreadyUsedException;
import com.chatop.back.user.exception.InvalidCredentialsException;
import com.chatop.back.user.exception.UserNotFoundException;
import com.chatop.back.user.model.User;
import com.chatop.back.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class AuthControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean AuthService authService;
    @MockitoBean UserService userService;

    @Test
    void register_returns_token() throws Exception {
        when(authService.register(any())).thenReturn("jwt-token");

        String body = objectMapper.writeValueAsString(Map.of(
                "name", "John", "email", "john@test.com", "password", "secret123"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void register_returns_400_on_missing_name() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com", "password", "secret123"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void register_returns_400_when_email_already_used() throws Exception {
        when(authService.register(any())).thenThrow(new EmailAlreadyUsedException("john@test.com"));

        String body = objectMapper.writeValueAsString(Map.of(
                "name", "John", "email", "john@test.com", "password", "secret123"));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void login_returns_token() throws Exception {
        when(authService.login(any())).thenReturn("jwt-token");

        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com", "password", "secret123"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void login_returns_401_on_bad_credentials() throws Exception {
        when(authService.login(any())).thenThrow(new InvalidCredentialsException());

        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com", "password", "wrong"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("error"));
    }

    @Test
    void me_returns_user_profile() throws Exception {
        User user = User.builder()
                .id(1L).name("John").email("john@test.com").password("hash")
                .createdAt(Instant.parse("2024-01-15T10:00:00Z"))
                .updatedAt(Instant.parse("2024-01-15T10:00:00Z"))
                .build();
        when(userService.getByEmail("john@test.com")).thenReturn(user);

        mockMvc.perform(get("/api/auth/me")
                        .with(jwt().jwt(j -> j.subject("john@test.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.created_at").exists());
    }

    @Test
    void me_returns_404_when_user_missing() throws Exception {
        when(userService.getByEmail("ghost@test.com"))
                .thenThrow(new UserNotFoundException("ghost@test.com"));

        mockMvc.perform(get("/api/auth/me")
                        .with(jwt().jwt(j -> j.subject("ghost@test.com"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void me_returns_401_without_jwt() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
