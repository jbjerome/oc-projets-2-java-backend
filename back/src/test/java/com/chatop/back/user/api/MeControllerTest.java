package com.chatop.back.user.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.user.api.controller.MeController;
import com.chatop.back.user.application.usecase.GetCurrentUserUseCase;
import com.chatop.back.user.domain.entity.User;
import com.chatop.back.user.domain.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Security filters enabled (we import {@link SecurityConfig}) so that the
 * mocked JWT produced by {@code jwt()} goes through the Spring Security
 * chain and feeds the controller's {@code @AuthenticationPrincipal}.
 */
@WebMvcTest(MeController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class MeControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean GetCurrentUserUseCase getCurrentUserUseCase;

    @Test
    void returns_200_with_user_profile() throws Exception {
        User existing = User.fromPersistence(
                1L, "John", "john@test.com", "hash",
                Instant.parse("2024-01-15T10:00:00Z"),
                Instant.parse("2024-01-15T10:00:00Z")
        );
        when(getCurrentUserUseCase.handle("john@test.com")).thenReturn(existing);

        mockMvc.perform(get("/api/auth/me")
                        .with(jwt().jwt(j -> j.subject("john@test.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@test.com"))
                .andExpect(jsonPath("$.created_at").exists())
                .andExpect(jsonPath("$.updated_at").exists());
    }

    @Test
    void returns_404_when_user_not_found() throws Exception {
        when(getCurrentUserUseCase.handle("ghost@test.com"))
                .thenThrow(new UserNotFoundException("ghost@test.com"));

        mockMvc.perform(get("/api/auth/me")
                        .with(jwt().jwt(j -> j.subject("ghost@test.com"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns_401_when_no_jwt() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}
