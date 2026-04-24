package com.chatop.back.rental.api.controller;

import com.chatop.back.rental.api.response.RentalResponse;
import com.chatop.back.rental.api.response.RentalsResponse;
import com.chatop.back.rental.application.usecase.ListRentalsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "Rental management endpoints")
public class ListRentalsController {

    private final ListRentalsUseCase listRentalsUseCase;

    public ListRentalsController(ListRentalsUseCase listRentalsUseCase) {
        this.listRentalsUseCase = listRentalsUseCase;
    }

    @GetMapping
    @Operation(summary = "List all rentals")
    public RentalsResponse list() {
        List<RentalResponse> rentals = listRentalsUseCase.handle().stream()
                .map(RentalResponse::from)
                .toList();
        return RentalsResponse.builder().rentals(rentals).build();
    }
}
