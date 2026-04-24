package com.chatop.back.user.application;

import com.chatop.back.auth.domain.TokenIssuer;
import com.chatop.back.user.application.command.RegisterUserCommand;
import com.chatop.back.user.application.usecase.RegisterUserUseCase;
import com.chatop.back.user.domain.exception.EmailAlreadyUsedException;
import com.chatop.back.user.domain.service.PasswordHasher;
import com.chatop.back.user.domain.entity.User;
import com.chatop.back.user.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock UserRepository userRepository;
    @Mock PasswordHasher passwordHasher;
    @Mock TokenIssuer tokenIssuer;

    @InjectMocks RegisterUserUseCase useCase;

    @Test
    void registers_new_user_hashes_password_and_returns_jwt() {
        RegisterUserCommand command = new RegisterUserCommand("John", "john@test.com", "secret123");
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordHasher.hash("secret123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(tokenIssuer.issue(any(User.class))).thenReturn("jwt-token");

        String token = useCase.handle(command);

        assertThat(token).isEqualTo("jwt-token");
        verify(userRepository).save(argThat(u ->
                u.name().equals("John")
                        && u.email().equals("john@test.com")
                        && u.passwordHash().equals("hashed")
        ));
    }

    @Test
    void throws_when_email_already_exists() {
        RegisterUserCommand command = new RegisterUserCommand("John", "john@test.com", "secret123");
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.handle(command))
                .isInstanceOf(EmailAlreadyUsedException.class)
                .hasMessageContaining("john@test.com");

        verify(userRepository, never()).save(any());
        verify(passwordHasher, never()).hash(any());
        verify(tokenIssuer, never()).issue(any());
    }
}
