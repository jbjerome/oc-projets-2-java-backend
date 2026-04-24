package com.chatop.back.rental.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.rental.api.controller.GetRentalController;
import com.chatop.back.rental.application.usecase.GetRentalUseCase;
import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.exception.RentalNotFoundException;
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

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetRentalController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class GetRentalControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean GetRentalUseCase getRentalUseCase;

    @Test
    void returns_one_rental() throws Exception {
        Rental rental = Rental.fromPersistence(
                7L, "House 7",
                Surface.of(BigDecimal.valueOf(100)),
                Price.of(BigDecimal.valueOf(200)),
                "http://minio/rentals/pic.jpg",
                "nice",
                42L,
                Instant.parse("2024-01-15T10:00:00Z"),
                Instant.parse("2024-01-15T10:00:00Z")
        );
        when(getRentalUseCase.handle(7L)).thenReturn(rental);

        mockMvc.perform(get("/api/rentals/7").with(jwt().jwt(j -> j.subject("u@t.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.name").value("House 7"));
    }

    @Test
    void returns_404_when_rental_missing() throws Exception {
        when(getRentalUseCase.handle(99L)).thenThrow(new RentalNotFoundException(99L));

        mockMvc.perform(get("/api/rentals/99").with(jwt().jwt(j -> j.subject("u@t.com"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void returns_401_when_no_jwt() throws Exception {
        mockMvc.perform(get("/api/rentals/1"))
                .andExpect(status().isUnauthorized());
    }
}
