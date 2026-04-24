package com.chatop.back.rental.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.rental.api.controller.UpdateRentalController;
import com.chatop.back.rental.application.command.UpdateRentalCommand;
import com.chatop.back.rental.application.usecase.UpdateRentalUseCase;
import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.exception.NotRentalOwnerException;
import com.chatop.back.rental.domain.vo.Price;
import com.chatop.back.rental.domain.vo.Surface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UpdateRentalController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class UpdateRentalControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean UpdateRentalUseCase updateRentalUseCase;

    @Test
    void updates_rental() throws Exception {
        Rental rental = Rental.fromPersistence(
                5L, "Updated",
                Surface.of(BigDecimal.valueOf(150)),
                Price.of(BigDecimal.valueOf(250)),
                "http://minio/rentals/pic.jpg",
                "updated desc",
                42L,
                Instant.parse("2024-01-15T10:00:00Z"),
                Instant.parse("2024-01-15T10:00:00Z")
        );
        when(updateRentalUseCase.handle(any(UpdateRentalCommand.class))).thenReturn(rental);

        mockMvc.perform(put("/api/rentals/5")
                        .contentType("application/x-www-form-urlencoded")
                        .param("name", "Updated")
                        .param("surface", "150")
                        .param("price", "250")
                        .param("description", "updated desc")
                        .with(jwt().jwt(j -> j.subject("owner@test.com").claim("user_id", 42L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rental updated !"));
    }

    @Test
    void returns_403_when_not_owner() throws Exception {
        when(updateRentalUseCase.handle(any(UpdateRentalCommand.class)))
                .thenThrow(new NotRentalOwnerException(5L));

        mockMvc.perform(put("/api/rentals/5")
                        .contentType("application/x-www-form-urlencoded")
                        .param("name", "Updated")
                        .param("surface", "150")
                        .param("price", "250")
                        .param("description", "updated desc")
                        .with(jwt().jwt(j -> j.subject("intruder@test.com").claim("user_id", 99L))))
                .andExpect(status().isForbidden());
    }
}
