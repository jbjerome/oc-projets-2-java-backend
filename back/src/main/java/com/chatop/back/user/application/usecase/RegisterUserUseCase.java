package com.chatop.back.user.application.usecase;

import com.chatop.back.auth.domain.TokenIssuer;
import com.chatop.back.user.application.command.RegisterUserCommand;
import com.chatop.back.user.domain.exception.EmailAlreadyUsedException;
import com.chatop.back.user.domain.service.PasswordHasher;
import com.chatop.back.user.domain.entity.User;
import com.chatop.back.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public RegisterUserUseCase(UserRepository userRepository,
                               PasswordHasher passwordHasher,
                               TokenIssuer tokenIssuer) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Transactional
    public String handle(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new EmailAlreadyUsedException(command.email());
        }
        User user = User.create(command.name(), command.email(), passwordHasher.hash(command.password()));
        User saved = userRepository.save(user);
        return tokenIssuer.issue(saved);
    }
}
