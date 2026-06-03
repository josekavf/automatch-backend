package com.automatch.iam_service.application.service;

import com.automatch.iam_service.domain.model.User;

public interface TokenService {
    String generateToken(User user);
}
