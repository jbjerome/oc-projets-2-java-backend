package com.chatop.back.user.application;

import com.chatop.back.auth.domain.TokenIssuer;
import com.chatop.back.user.domain.InvalidCredentialsException;
import com.chatop.back.user.domain.PasswordHasher;
import com.chatop.back.user.domain.User;
import com.chatop.back.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoginUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenIssuer tokenIssuer;

    public LoginUserUseCase(UserRepository userRepository,
                            PasswordHasher passwordHasher,
                            TokenIssuer tokenIssuer) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenIssuer = tokenIssuer;
    }

    @Transactional(readOnly = true)
    public String handle(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.password(), user.passwordHash())) {
            throw new InvalidCredentialsException();
        }

        return tokenIssuer.issue(user);
    }
}
