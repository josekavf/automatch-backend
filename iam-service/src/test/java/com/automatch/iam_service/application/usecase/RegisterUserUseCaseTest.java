package com.automatch.iam_service.application.usecase;

import com.automatch.iam_service.application.dto.request.RegisterUserRequest;
import com.automatch.iam_service.application.service.EventPublisher;
import com.automatch.iam_service.domain.model.Role;
import com.automatch.iam_service.domain.model.User;
import com.automatch.iam_service.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EventPublisher eventPublisher;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    private RegisterUserRequest request;

    @BeforeEach
    void setUp() {
        request = new RegisterUserRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setRole(Role.CLIENT);
    }

    @Test
    void execute_WhenEmailDoesNotExist_ShouldSaveUserAndPublishEvent() {
        // Preparação
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Execução
        User result = registerUserUseCase.execute(request);

        // Verificação
        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(request.getEmail(), result.getEmail());
        
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository).save(any(User.class));
        verify(eventPublisher).publishUserRegistered(any());
    }

    @Test
    void execute_WhenEmailExists_ShouldThrowException() {
        // Preparação
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Execução & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> registerUserUseCase.execute(request));
        assertEquals("E-mail já cadastrado", exception.getMessage());
        
        verify(userRepository).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishUserRegistered(any());
    }
}
