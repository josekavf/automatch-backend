package com.automatch.iam_service.application.validator;

import com.automatch.iam_service.application.dto.request.RegisterUserRequest;
import com.automatch.iam_service.domain.exception.EmailAlreadyExistsException;
import com.automatch.iam_service.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailUniquenessValidator implements UserRegistrationValidator {
    private final UserRepository userRepository;

    @Override
    public void validate(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException();
        }
    }
}
