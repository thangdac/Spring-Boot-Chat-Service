package com.dt.chat_service.exception;

import java.util.Map;
import java.util.Objects;

import jakarta.validation.ConstraintViolation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.dt.chat_service.dto.response.APIResponse;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse<Object>> handleException(Exception ex) {
        log.error("Unexpected error: ", ex);

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(ErrorCode.INTERNAL_ERROR.getCode());
        response.setMessage(ErrorCode.INTERNAL_ERROR.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse<Object>> handleAppException(AppException ex) {

        ErrorCode errorCode = ex.getErrorCode();

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(response);
    }

    // lỗi validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        String enumKey =
                Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();

        ErrorCode errorCode = ErrorCode.INVALID_INPUT;

        Map<String, Object> attributes = null;

        try {

            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolations =
                    ex.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);

            @SuppressWarnings("unchecked")
            Map<String, Object> attrs = (Map<String, Object>)
                    constraintViolations.getConstraintDescriptor().getAttributes();
            attributes = attrs;

        } catch (IllegalArgumentException e) {
            log.warn("Failed to get error attributes for enum key: {}", enumKey, e);
        }

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(errorCode.getCode());
        response.setMessage(
                Objects.nonNull(attributes)
                        ? parseMessage(errorCode.getMessage(), attributes)
                        : errorCode.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    private String parseMessage(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        message = message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
        return message;
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<APIResponse<Object>> handleAccessDeniedException(AccessDeniedException ex) {
        APIResponse<Object> response = new APIResponse<>();
        response.setCode(ErrorCode.UNAUTHORIZED.getCode());
        response.setMessage(ErrorCode.UNAUTHORIZED.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(value = AuthenticationException.class)
    ResponseEntity<APIResponse<Object>> handleAuthenticationException(AuthenticationException ex) {
        APIResponse<Object> response = new APIResponse<>();
        response.setCode(ErrorCode.UNAUTHENTICATED.getCode());
        response.setMessage(ErrorCode.UNAUTHENTICATED.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
