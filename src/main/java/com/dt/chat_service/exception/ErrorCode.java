package com.dt.chat_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public enum     ErrorCode {

    // Validation
    INVALID_INPUT(400, "Invalid input data", HttpStatus.BAD_REQUEST),
    USERNAME_INVALID(400, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(400, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(400, "Email is not valid", HttpStatus.BAD_REQUEST),
    USERNAME_BLANK(400, "Username is required", HttpStatus.BAD_REQUEST),
    PASSWORD_BLANK(400, "Password is required", HttpStatus.BAD_REQUEST),

    // Business
    USER_NOT_FOUND(1001, "User not found", HttpStatus.NOT_FOUND),
    USER_ALREADY_EXISTS(1002, "User already exists", HttpStatus.CONFLICT),
    UNAUTHORIZED(1003, "Access denied", HttpStatus.FORBIDDEN),

    // System
    INTERNAL_ERROR(5000, "Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    // Chat
    MESSAGE_NOT_FOUND(2001, "Message not found", HttpStatus.NOT_FOUND),
    ROOM_NOT_FOUND(2002, "Chat room not found", HttpStatus.NOT_FOUND),
    ROOM_ALREADY_EXISTS(2003, "Chat room already exists", HttpStatus.CONFLICT);

    private final int code;
    private final String message;
    private final HttpStatus status;

        ErrorCode(int code, String message, HttpStatus status) {
            this.code = code;
            this.message = message;
            this.status = status;
        }


}
