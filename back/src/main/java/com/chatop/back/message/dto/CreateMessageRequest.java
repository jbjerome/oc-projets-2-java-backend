package com.chatop.back.message.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateMessageRequest {

    @NotNull
    @JsonProperty("user_id")
    private Long userId;

    @NotNull
    @JsonProperty("rental_id")
    private Long rentalId;

    @NotBlank
    @Size(max = 2000)
    private String message;
}
