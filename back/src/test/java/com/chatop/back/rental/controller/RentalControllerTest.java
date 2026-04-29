package com.chatop.back.rental.controller;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.rental.exception.NotRentalOwnerException;
import com.chatop.back.rental.exception.RentalNotFoundException;
import com.chatop.back.rental.model.Price;
import com.chatop.back.rental.model.Rental;
import com.chatop.back.rental.model.Surface;
import com.chatop.back.rental.service.RentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RentalController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class RentalControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean RentalService rentalService;

    private static Rental sampleRental(Long id) {
        return Rental.builder()
                .id(id).name("House " + id)
                .surface(Surface.of(BigDecimal.valueOf(100)))
                .price(Price.of(BigDecimal.valueOf(200)))
                .picture("http://minio/rentals/pic.jpg")
                .description("nice")
                .ownerId(42L)
                .createdAt(Instant.parse("2024-01-15T10:00:00Z"))
                .updatedAt(Instant.parse("2024-01-15T10:00:00Z"))
                .build();
    }

    @Test
    void list_returns_all() throws Exception {
        when(rentalService.findAll()).thenReturn(List.of(sampleRental(1L), sampleRental(2L)));

        mockMvc.perform(get("/api/rentals").with(jwt().jwt(j -> j.subject("u@t.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rentals.length()").value(2))
                .andExpect(jsonPath("$.rentals[0].id").value(1))
                .andExpect(jsonPath("$.rentals[0].owner_id").value(42));
    }

    @Test
    void list_returns_401_without_jwt() throws Exception {
        mockMvc.perform(get("/api/rentals"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getById_returns_rental() throws Exception {
        when(rentalService.getById(7L)).thenReturn(sampleRental(7L));

        mockMvc.perform(get("/api/rentals/7").with(jwt().jwt(j -> j.subject("u@t.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7));
    }

    @Test
    void getById_returns_404_when_missing() throws Exception {
        when(rentalService.getById(99L)).thenThrow(new RentalNotFoundException(99L));

        mockMvc.perform(get("/api/rentals/99").with(jwt().jwt(j -> j.subject("u@t.com"))))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_uploads_picture_and_returns_message() throws Exception {
        when(rentalService.create(any(), eq(42L))).thenReturn(sampleRental(1L));

        MockMultipartFile picture = new MockMultipartFile(
                "picture", "house.jpg", "image/jpeg", "fakebytes".getBytes());

        mockMvc.perform(multipart("/api/rentals")
                        .file(picture)
                        .param("name", "Nice house")
                        .param("surface", "100")
                        .param("price", "200")
                        .param("description", "very nice")
                        .with(jwt().jwt(j -> j.subject("o@t.com").claim("user_id", 42L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rental created !"));

        verify(rentalService).create(any(), eq(42L));
    }

    @Test
    void update_returns_message() throws Exception {
        when(rentalService.update(eq(5L), any(), eq(42L))).thenReturn(sampleRental(5L));

        mockMvc.perform(put("/api/rentals/5")
                        .contentType("application/x-www-form-urlencoded")
                        .param("name", "Updated")
                        .param("surface", "150")
                        .param("price", "250")
                        .param("description", "updated desc")
                        .with(jwt().jwt(j -> j.subject("o@t.com").claim("user_id", 42L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rental updated !"));
    }

    @Test
    void update_returns_403_when_not_owner() throws Exception {
        when(rentalService.update(eq(5L), any(), eq(99L)))
                .thenThrow(new NotRentalOwnerException(5L));

        mockMvc.perform(put("/api/rentals/5")
                        .contentType("application/x-www-form-urlencoded")
                        .param("name", "Updated")
                        .param("surface", "150")
                        .param("price", "250")
                        .param("description", "updated desc")
                        .with(jwt().jwt(j -> j.subject("i@t.com").claim("user_id", 99L))))
                .andExpect(status().isForbidden());
    }
}
