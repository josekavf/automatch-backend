package com.automatch.iam_service.domain.exception;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super("E-mail já cadastrado");
    }
}
