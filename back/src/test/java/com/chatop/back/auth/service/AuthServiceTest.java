package com.chatop.back.auth.service;

import com.chatop.back.user.dto.LoginRequest;
import com.chatop.back.user.dto.RegisterRequest;
import com.chatop.back.user.exception.EmailAlreadyUsedException;
import com.chatop.back.user.exception.InvalidCredentialsException;
import com.chatop.back.user.model.User;
import com.chatop.back.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository userRepository;
    @Mock PasswordEncoder passwordEncoder;
    @Mock JwtTokenService jwtTokenService;

    @InjectMocks AuthService authService;

    @Test
    void register_creates_user_and_returns_jwt() {
        RegisterRequest request = RegisterRequest.builder()
                .name("John").email("john@test.com").password("secret123").build();
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        when(jwtTokenService.issue(any(User.class))).thenReturn("jwt-token");

        String token = authService.register(request);

        assertThat(token).isEqualTo("jwt-token");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_throws_when_email_already_used() {
        RegisterRequest request = RegisterRequest.builder()
                .name("John").email("john@test.com").password("secret123").build();
        when(userRepository.existsByEmail("john@test.com")).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(EmailAlreadyUsedException.class)
                .hasMessageContaining("john@test.com");

        verify(userRepository, never()).save(any());
        verify(jwtTokenService, never()).issue(any());
    }

    @Test
    void login_returns_jwt_when_credentials_match() {
        LoginRequest request = LoginRequest.builder().email("john@test.com").password("secret123").build();
        User existing = User.builder().id(1L).email("john@test.com").password("hashed").build();
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("secret123", "hashed")).thenReturn(true);
        when(jwtTokenService.issue(existing)).thenReturn("jwt-token");

        assertThat(authService.login(request)).isEqualTo("jwt-token");
    }

    @Test
    void login_throws_when_email_unknown() {
        LoginRequest request = LoginRequest.builder().email("ghost@test.com").password("secret").build();
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(jwtTokenService, never()).issue(any());
    }

    @Test
    void login_throws_when_password_does_not_match() {
        LoginRequest request = LoginRequest.builder().email("john@test.com").password("wrong").build();
        User existing = User.builder().id(1L).email("john@test.com").password("hashed").build();
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidCredentialsException.class);

        verify(jwtTokenService, never()).issue(any());
    }
}
