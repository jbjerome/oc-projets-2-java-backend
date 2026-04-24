package com.chatop.back.rental.api.controller;

import com.chatop.back.rental.api.request.UpdateRentalRequest;
import com.chatop.back.shared.api.response.ApiMessage;
import com.chatop.back.rental.application.command.UpdateRentalCommand;
import com.chatop.back.rental.application.usecase.UpdateRentalUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "Rental management endpoints")
public class UpdateRentalController {

    private final UpdateRentalUseCase updateRentalUseCase;

    public UpdateRentalController(UpdateRentalUseCase updateRentalUseCase) {
        this.updateRentalUseCase = updateRentalUseCase;
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing rental")
    public ApiMessage update(@PathVariable Long id,
                                  @Valid @ModelAttribute UpdateRentalRequest request,
                                  @AuthenticationPrincipal Jwt jwt) {
        updateRentalUseCase.handle(new UpdateRentalCommand(
                id,
                request.getName(),
                request.getSurface(),
                request.getPrice(),
                request.getDescription(),
                currentUserId(jwt)
        ));
        return ApiMessage.builder().message("Rental updated !").build();
    }

    private static Long currentUserId(Jwt jwt) {
        return ((Number) jwt.getClaim("user_id")).longValue();
    }
}
