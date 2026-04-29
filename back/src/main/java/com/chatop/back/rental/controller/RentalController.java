package com.chatop.back.rental.controller;

import com.chatop.back.rental.dto.CreateRentalRequest;
import com.chatop.back.rental.dto.RentalResponse;
import com.chatop.back.rental.dto.RentalsResponse;
import com.chatop.back.rental.dto.UpdateRentalRequest;
import com.chatop.back.rental.service.RentalService;
import com.chatop.back.shared.dto.ApiMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "Rental management endpoints")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    @Operation(summary = "List all rentals")
    public RentalsResponse list() {
        List<RentalResponse> rentals = rentalService.findAll().stream()
                .map(RentalResponse::from)
                .toList();
        return RentalsResponse.builder().rentals(rentals).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a rental by id")
    public RentalResponse getById(@PathVariable Long id) {
        return RentalResponse.from(rentalService.getById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new rental")
    public ApiMessage create(@Valid @ModelAttribute CreateRentalRequest request,
                             @AuthenticationPrincipal Jwt jwt) {
        rentalService.create(request, currentUserId(jwt));
        return ApiMessage.builder().message("Rental created !").build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing rental")
    public ApiMessage update(@PathVariable Long id,
                             @Valid @ModelAttribute UpdateRentalRequest request,
                             @AuthenticationPrincipal Jwt jwt) {
        rentalService.update(id, request, currentUserId(jwt));
        return ApiMessage.builder().message("Rental updated !").build();
    }

    private static Long currentUserId(Jwt jwt) {
        return ((Number) jwt.getClaim("user_id")).longValue();
    }
}
