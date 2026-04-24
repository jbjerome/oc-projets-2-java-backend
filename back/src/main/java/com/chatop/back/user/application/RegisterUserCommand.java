package com.chatop.back.user.application;

public record RegisterUserCommand(String name, String email, String password) {}
