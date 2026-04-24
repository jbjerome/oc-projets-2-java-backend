package com.chatop.back.rental.api;

import com.chatop.back.config.GlobalExceptionHandler;
import com.chatop.back.config.SecurityConfig;
import com.chatop.back.rental.api.controller.CreateRentalController;
import com.chatop.back.rental.application.command.CreateRentalCommand;
import com.chatop.back.rental.application.usecase.CreateRentalUseCase;
import com.chatop.back.rental.domain.entity.Rental;
import com.chatop.back.rental.domain.vo.Price;
import com.chatop.back.rental.domain.vo.Surface;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateRentalController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class CreateRentalControllerTest {

    @Autowired MockMvc mockMvc;

    @MockitoBean CreateRentalUseCase createRentalUseCase;

    @Test
    void creates_rental() throws Exception {
        Rental rental = Rental.fromPersistence(
                1L, "Nice house",
                Surface.of(BigDecimal.valueOf(100)),
                Price.of(BigDecimal.valueOf(200)),
                "http://minio/rentals/pic.jpg",
                "very nice",
                42L,
                Instant.parse("2024-01-15T10:00:00Z"),
                Instant.parse("2024-01-15T10:00:00Z")
        );
        when(createRentalUseCase.handle(any(CreateRentalCommand.class))).thenReturn(rental);

        MockMultipartFile picture = new MockMultipartFile(
                "picture", "house.jpg", "image/jpeg", "fakebytes".getBytes()
        );

        mockMvc.perform(multipart("/api/rentals")
                        .file(picture)
                        .param("name", "Nice house")
                        .param("surface", "100")
                        .param("price", "200")
                        .param("description", "very nice")
                        .with(jwt().jwt(j -> j.subject("owner@test.com").claim("user_id", 42L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Rental created !"));

        verify(createRentalUseCase).handle(any(CreateRentalCommand.class));
    }
}
