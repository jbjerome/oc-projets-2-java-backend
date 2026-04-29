package com.chatop.back.user.service;

import com.chatop.back.user.exception.UserNotFoundException;
import com.chatop.back.user.model.User;
import com.chatop.back.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserRepository userRepository;

    @InjectMocks UserService userService;

    @Test
    void getByEmail_returns_user_when_found() {
        User existing = User.builder().id(1L).email("john@test.com").build();
        when(userRepository.findByEmail("john@test.com")).thenReturn(Optional.of(existing));

        assertThat(userService.getByEmail("john@test.com")).isSameAs(existing);
    }

    @Test
    void getByEmail_throws_when_missing() {
        when(userRepository.findByEmail("ghost@test.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getByEmail("ghost@test.com"))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getById_returns_user_when_found() {
        User existing = User.builder().id(7L).build();
        when(userRepository.findById(7L)).thenReturn(Optional.of(existing));

        assertThat(userService.getById(7L)).isSameAs(existing);
    }

    @Test
    void getById_throws_when_missing() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
