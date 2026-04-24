package com.chatop.back.rental.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.rental.api.controller.ListRentalsController;
import com.chatop.back.rental.application.usecase.ListRentalsUseCase;
import com.chatop.back.rental.domain.entity.Rental;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListRentalsController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class ListRentalsControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean ListRentalsUseCase listRentalsUseCase;

    private static Rental sampleRental(Long id) {
        return Rental.fromPersistence(
                id, "House " + id,
                Surface.of(BigDecimal.valueOf(100)),
                Price.of(BigDecimal.valueOf(200)),
                "http://minio/rentals/pic.jpg",
                "nice",
                42L,
                Instant.parse("2024-01-15T10:00:00Z"),
                Instant.parse("2024-01-15T10:00:00Z")
        );
    }

    @Test
    void lists_rentals() throws Exception {
        when(listRentalsUseCase.handle()).thenReturn(List.of(sampleRental(1L), sampleRental(2L)));

        mockMvc.perform(get("/api/rentals").with(jwt().jwt(j -> j.subject("u@t.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentals.length()").value(2))
                .andExpect(jsonPath("$.rentals[0].id").value(1))
                .andExpect(jsonPath("$.rentals[0].owner_id").value(42));
    }

    @Test
    void returns_401_when_no_jwt() throws Exception {
        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().isUnauthorized());
    }
}
