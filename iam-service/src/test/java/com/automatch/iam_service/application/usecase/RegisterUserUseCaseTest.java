package com.automatch.iam_service.application.usecase;

import com.automatch.iam_service.application.dto.request.RegisterUserRequest;
import com.automatch.iam_service.application.service.EventPublisher;
import com.automatch.iam_service.domain.model.Role;
import com.automatch.iam_service.domain.model.User;
import com.automatch.iam_service.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @Mock
    private com.automatch.iam_service.application.validator.UserRegistrationValidator validator;

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
        registerUserUseCase = new RegisterUserUseCase(userRepository, passwordEncoder, eventPublisher, java.util.List.of(validator));
    }

    @Test
    void execute_WhenValid_ShouldSaveUserAndPublishEvent() {
        doNothing().when(validator).validate(request);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();
        
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = registerUserUseCase.execute(request);

        assertNotNull(result);
        assertEquals(savedUser.getId(), result.getId());
        assertEquals(request.getEmail(), result.getEmail());
        
        verify(validator).validate(request);
        verify(userRepository).save(any(User.class));
        verify(eventPublisher).publishUserRegistered(any());
    }

    @Test
    void execute_WhenInvalid_ShouldThrowException() {
        doThrow(new com.automatch.iam_service.domain.exception.EmailAlreadyExistsException()).when(validator).validate(request);

        com.automatch.iam_service.domain.exception.EmailAlreadyExistsException exception = assertThrows(com.automatch.iam_service.domain.exception.EmailAlreadyExistsException.class, () -> registerUserUseCase.execute(request));
        assertEquals("E-mail já cadastrado", exception.getMessage());
        
        verify(validator).validate(request);
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publishUserRegistered(any());
    }
}
