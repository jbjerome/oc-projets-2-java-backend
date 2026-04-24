package com.chatop.back.user.application;

import com.chatop.back.user.domain.User;
import com.chatop.back.user.domain.UserNotFoundException;
import com.chatop.back.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCurrentUserUseCaseTest {

    @Mock UserRepository userRepository;

    @InjectMocks GetCurrentUserUseCase useCase;

    @Test
    void returns_user_when_found() {
        User existing = User.fromPersistence(
                1L, "John", "john@test.com", "hash",
                Instant.now(), Instant.now()
        );
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(existing));

        User result = useCase.handle("john@test.com");

        assertThat(result).isSameAs(existing);
    }

    @Test
    void throws_when_email_does_not_exist() {
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.handle("ghost@test.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("ghost@test.com");
    }
}
