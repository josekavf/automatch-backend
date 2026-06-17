package com.automatch.iam_service.application.usecase;

import com.automatch.iam_service.application.dto.request.RegisterUserRequest;
import com.automatch.iam_service.application.service.EventPublisher;
import com.automatch.iam_service.domain.event.UserRegisteredEvent;
import com.automatch.iam_service.domain.model.User;
import com.automatch.iam_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import com.automatch.iam_service.application.validator.UserRegistrationValidator;

@Service
@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventPublisher eventPublisher;
    private final List<UserRegistrationValidator> validators;

    @Transactional
    public User execute(RegisterUserRequest request) {
        validators.forEach(v -> v.validate(request));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        eventPublisher.publishUserRegistered(UserRegisteredEvent.builder()
                .userId(savedUser.getId())
                .email(savedUser.getEmail())
                .firstName(savedUser.getFirstName())
                .lastName(savedUser.getLastName())
                .role(savedUser.getRole().name())
                .build());

        return savedUser;
    }
}
