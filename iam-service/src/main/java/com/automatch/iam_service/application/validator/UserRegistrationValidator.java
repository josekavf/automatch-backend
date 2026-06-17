package com.automatch.iam_service.application.validator;

import com.automatch.iam_service.application.dto.request.RegisterUserRequest;

public interface UserRegistrationValidator {
    void validate(RegisterUserRequest request);
}
