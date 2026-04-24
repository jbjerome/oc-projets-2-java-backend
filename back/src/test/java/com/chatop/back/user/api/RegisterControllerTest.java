package com.chatop.back.user.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.user.application.RegisterUserCommand;
import com.chatop.back.user.application.RegisterUserUseCase;
import com.chatop.back.user.domain.EmailAlreadyUsedException;
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

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class RegisterControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean RegisterUserUseCase registerUserUseCase;

    @Test
    void returns_200_with_token_on_valid_payload() throws Exception {
        when(registerUserUseCase.handle(any(RegisterUserCommand.class))).thenReturn("jwt-token");

        String body = objectMapper.writeValueAsString(Map.of(
                "name", "John",
                "email", "john@test.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    void returns_400_on_missing_name() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "email", "john@test.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists());
    }

    @Test
    void returns_400_on_malformed_email() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "John",
                "email", "not-an-email",
                "password", "secret123"
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    void returns_400_on_password_too_short() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "name", "John",
                "email", "john@test.com",
                "password", "123"
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.password").exists());
    }

    @Test
    void returns_400_when_email_already_used() throws Exception {
        when(registerUserUseCase.handle(any(RegisterUserCommand.class)))
                .thenThrow(new EmailAlreadyUsedException("john@test.com"));

        String body = objectMapper.writeValueAsString(Map.of(
                "name", "John",
                "email", "john@test.com",
                "password", "secret123"
        ));

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }
}
