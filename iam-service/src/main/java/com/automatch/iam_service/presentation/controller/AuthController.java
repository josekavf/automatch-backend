package com.automatch.iam_service.presentation.controller;

import com.automatch.iam_service.application.dto.request.LoginRequest;
import com.automatch.iam_service.application.dto.request.RegisterUserRequest;
import com.automatch.iam_service.application.dto.response.AuthResponse;
import com.automatch.iam_service.application.usecase.AuthenticateUserUseCase;
import com.automatch.iam_service.application.usecase.RegisterUserUseCase;
import com.automatch.iam_service.domain.model.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticateUserUseCase authenticateUserUseCase;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account (Client or Mechanic)")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserUseCase.execute(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Authenticates a user and returns a JWT token")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authenticateUserUseCase.execute(request));
    }
}
