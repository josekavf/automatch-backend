package com.automatch.iam_service.application.service;

import com.automatch.iam_service.domain.event.UserRegisteredEvent;

public interface EventPublisher {
    void publishUserRegistered(UserRegisteredEvent event);
}
