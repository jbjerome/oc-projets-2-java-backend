package com.chatop.back.rental.api.controller;

import com.chatop.back.rental.api.request.CreateRentalRequest;
import com.chatop.back.rental.api.response.MessageResponse;
import com.chatop.back.rental.application.command.CreateRentalCommand;
import com.chatop.back.rental.application.usecase.CreateRentalUseCase;
import com.chatop.back.rental.domain.vo.PictureUpload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Rental", description = "Rental management endpoints")
public class CreateRentalController {

    private final CreateRentalUseCase createRentalUseCase;

    public CreateRentalController(CreateRentalUseCase createRentalUseCase) {
        this.createRentalUseCase = createRentalUseCase;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Create a new rental")
    public MessageResponse create(@Valid @ModelAttribute CreateRentalRequest request,
                                  @AuthenticationPrincipal Jwt jwt) throws IOException {
        createRentalUseCase.handle(new CreateRentalCommand(
                request.getName(),
                request.getSurface(),
                request.getPrice(),
                request.getDescription(),
                toPictureUpload(request.getPicture()),
                currentUserId(jwt)
        ));
        return MessageResponse.builder().message("Rental created !").build();
    }

    private static PictureUpload toPictureUpload(MultipartFile file) throws IOException {
        String contentType = file.getContentType() != null ? file.getContentType() : "application/octet-stream";
        String filename = file.getOriginalFilename() != null ? file.getOriginalFilename() : "upload";
        return new PictureUpload(file.getBytes(), contentType, filename);
    }

    private static Long currentUserId(Jwt jwt) {
        return ((Number) jwt.getClaim("user_id")).longValue();
    }
}
