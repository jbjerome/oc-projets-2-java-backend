package com.chatop.back.user.application.command;

public record RegisterUserCommand(String name, String email, String password) {}
