package com.chatop.back.message.api.controller;

import com.chatop.back.message.api.request.CreateMessageRequest;
import com.chatop.back.message.api.response.MessageResponse;
import com.chatop.back.message.application.command.SendMessageCommand;
import com.chatop.back.message.application.usecase.SendMessageUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/messages")
@Tag(name = "Message", description = "Send messages to rental owners")
public class SendMessageController {

    private final SendMessageUseCase sendMessageUseCase;

    public SendMessageController(SendMessageUseCase sendMessageUseCase) {
        this.sendMessageUseCase = sendMessageUseCase;
    }

    @PostMapping
    @Operation(summary = "Send a message to a rental owner")
    public MessageResponse send(@Valid @RequestBody CreateMessageRequest request) {
        sendMessageUseCase.handle(new SendMessageCommand(
                request.getUserId(),
                request.getRentalId(),
                request.getMessage()
        ));
        return MessageResponse.builder().message("Message send with success").build();
    }
}
