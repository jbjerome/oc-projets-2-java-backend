package com.chatop.back.message.controller;

import com.chatop.back.message.dto.CreateMessageRequest;
import com.chatop.back.message.service.MessageService;
import com.chatop.back.shared.dto.ApiMessage;
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
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    @Operation(summary = "Send a message to a rental owner")
    public ApiMessage send(@Valid @RequestBody CreateMessageRequest request) {
        messageService.send(request);
        return ApiMessage.builder().message("Message send with success").build();
    }
}
