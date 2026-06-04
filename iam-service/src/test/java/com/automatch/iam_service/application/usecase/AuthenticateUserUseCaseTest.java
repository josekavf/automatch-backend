package com.automatch.iam_service.application.usecase;

import com.automatch.iam_service.application.dto.request.LoginRequest;
import com.automatch.iam_service.application.dto.response.AuthResponse;
import com.automatch.iam_service.application.service.TokenService;
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

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    private LoginRequest request;
    private User user;

    @BeforeEach
    void setUp() {
        request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        user = User.builder()
                .id(UUID.randomUUID())
                .email("test@example.com")
                .password("encodedPassword")
                .role(Role.CLIENT)
                .build();
    }

    @Test
    void execute_WhenCredentialsAreValid_ShouldReturnAuthResponse() {
        // Preparação
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(true);
        when(tokenService.generateToken(user)).thenReturn("jwtToken");

        // Execução
        AuthResponse result = authenticateUserUseCase.execute(request);

        // Verificação
        assertNotNull(result);
        assertEquals("jwtToken", result.getToken());
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getRole().name(), result.getRole());
        
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(tokenService).generateToken(user);
    }

    @Test
    void execute_WhenUserNotFound_ShouldThrowException() {
        // Preparação
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Execução & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticateUserUseCase.execute(request));
        assertEquals("Invalid credentials", exception.getMessage());
        
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void execute_WhenPasswordIsIncorrect_ShouldThrowException() {
        // Preparação
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword())).thenReturn(false);

        // Execução & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authenticateUserUseCase.execute(request));
        assertEquals("Invalid credentials", exception.getMessage());
        
        verify(userRepository).findByEmail(request.getEmail());
        verify(passwordEncoder).matches(request.getPassword(), user.getPassword());
        verify(tokenService, never()).generateToken(any());
    }
}
