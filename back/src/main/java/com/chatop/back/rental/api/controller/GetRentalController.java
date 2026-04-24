package com.chatop.back.rental.api.controller;

import com.chatop.back.rental.api.response.RentalResponse;
import com.chatop.back.rental.application.usecase.GetRentalUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "Rental management endpoints")
public class GetRentalController {

    private final GetRentalUseCase getRentalUseCase;

    public GetRentalController(GetRentalUseCase getRentalUseCase) {
        this.getRentalUseCase = getRentalUseCase;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a rental by id")
    public RentalResponse getById(@PathVariable Long id) {
        return RentalResponse.from(getRentalUseCase.handle(id));
    }
}
