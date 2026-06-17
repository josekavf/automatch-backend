package com.automatch.iam_service.presentation.exception;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();
    private static final String TRACE_ID = "test-trace-id";

    @BeforeEach
    void setUp() {
        MDC.put("traceId", TRACE_ID);
    }

    @AfterEach
    void tearDown() {
        MDC.clear();
    }

    @Test
    void shouldHandleBusinessException() {
        com.automatch.iam_service.domain.exception.BusinessException exception = new com.automatch.iam_service.domain.exception.BusinessException("Business error message");
        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleBusinessException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Business Rule Violation", response.getBody().getError());
        assertEquals("Business error message", response.getBody().getMessage());
    }

    @Test
    void handleValidationException_ShouldReturnBadRequestWithDetails() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "must not be blank");
        
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));

        ResponseEntity<GlobalExceptionHandler.ErrorResponse> response = handler.handleValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("field: must not be blank"));
        assertEquals(TRACE_ID, response.getBody().getTraceId());
    }
}
