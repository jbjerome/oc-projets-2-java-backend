package com.chatop.back.user.application;

import com.chatop.back.auth.domain.TokenIssuer;
import com.chatop.back.user.application.command.LoginUserCommand;
import com.chatop.back.user.application.usecase.LoginUserUseCase;
import com.chatop.back.user.domain.exception.InvalidCredentialsException;
import com.chatop.back.user.domain.service.PasswordHasher;
import com.chatop.back.user.domain.entity.User;
import com.chatop.back.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginUserUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock PasswordHasher passwordHasher;
    @Mock TokenIssuer tokenIssuer;

    @InjectMocks LoginUserUseCase useCase;

    private final User existingUser = User.fromPersistence(
            1L, "John", "john@test.com", "hashed",
            Instant.parse("2024-01-01T00:00:00Z"),
            Instant.parse("2024-01-01T00:00:00Z")
    );

    @Test
    void returns_jwt_when_credentials_match() {
        LoginUserCommand command = new LoginUserCommand("john@test.com", "secret123");
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches("secret123", "hashed")).thenReturn(true);
        when(tokenIssuer.issue(existingUser)).thenReturn("jwt-token");

        String token = useCase.handle(command);

        assertThat(token).isEqualTo("jwt-token");
    }

    @Test
    void throws_when_email_is_unknown() {
        LoginUserCommand command = new LoginUserCommand("ghost@test.com", "secret123");
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.handle(command))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(passwordHasher, never()).matches(any(), any());
        verify(tokenIssuer, never()).issue(any());
    }

    @Test
    void throws_when_password_does_not_match() {
        LoginUserCommand command = new LoginUserCommand("john@test.com", "wrong");
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> useCase.handle(command))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(tokenIssuer, never()).issue(any());
    }
}
