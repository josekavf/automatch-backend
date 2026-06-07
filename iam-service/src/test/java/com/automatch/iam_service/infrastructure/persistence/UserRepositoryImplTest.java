package com.automatch.iam_service.infrastructure.persistence;

import com.automatch.iam_service.domain.model.Role;
import com.automatch.iam_service.domain.model.User;
import com.automatch.iam_service.infrastructure.persistence.entity.UserEntity;
import com.automatch.iam_service.infrastructure.persistence.repository.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private JpaUserRepository jpaUserRepository;

    @InjectMocks
    private UserRepositoryImpl userRepository;

    private User user;
    private UserEntity userEntity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CLIENT)
                .build();

        userEntity = UserEntity.builder()
                .id(userId)
                .email("test@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .role(Role.CLIENT)
                .build();
    }

    @Test
    void save_ShouldReturnDomainUser() {
        when(jpaUserRepository.save(any(UserEntity.class))).thenReturn(userEntity);

        User savedUser = userRepository.save(user);

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getId(), savedUser.getId());
        verify(jpaUserRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenExists() {
        when(jpaUserRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(userEntity));

        Optional<User> foundUser = userRepository.findByEmail(user.getEmail());

        assertTrue(foundUser.isPresent());
        assertEquals(user.getEmail(), foundUser.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenNotExists() {
        when(jpaUserRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertFalse(foundUser.isPresent());
    }

    @Test
    void findById_ShouldReturnUser_WhenExists() {
        when(jpaUserRepository.findById(userId)).thenReturn(Optional.of(userEntity));

        Optional<User> foundUser = userRepository.findById(userId);

        assertTrue(foundUser.isPresent());
        assertEquals(userId, foundUser.get().getId());
    }

    @Test
    void existsByEmail_ShouldReturnTrue_WhenExists() {
        when(jpaUserRepository.existsByEmail(user.getEmail())).thenReturn(true);

        boolean exists = userRepository.existsByEmail(user.getEmail());

        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenNotExists() {
        when(jpaUserRepository.existsByEmail(anyString())).thenReturn(false);

        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        assertFalse(exists);
    }
}
