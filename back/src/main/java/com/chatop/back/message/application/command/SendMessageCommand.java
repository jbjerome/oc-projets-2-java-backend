package com.chatop.back.message.application.command;

public record SendMessageCommand(Long userId, Long rentalId, String content) {}
