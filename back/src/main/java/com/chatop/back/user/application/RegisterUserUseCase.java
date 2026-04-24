package com.chatop.back.user.application;

import com.chatop.back.auth.domain.TokenIssuer;
import com.chatop.back.user.domain.EmailAlreadyUsedException;
import com.chatop.back.user.domain.PasswordHasher;
import com.chatop.back.user.domain.User;
import com.chatop.back.user.domain.UserRepository;
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
