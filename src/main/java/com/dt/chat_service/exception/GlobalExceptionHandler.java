package com.dt.chat_service.exception;

import com.dt.chat_service.dto.response.APIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //lỗi không mong muốn
    @ExceptionHandler(value = Exception.class)
    ResponseEntity<APIResponse<Object>> handleException(Exception ex) {
        log.error("Unexpected error: ", ex);

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(ErrorCode.INTERNAL_ERROR.getCode());
        response.setMessage(ErrorCode.INTERNAL_ERROR.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }

    //lỗi nghiệp vụ
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<APIResponse<Object>> handleAppException(AppException ex) {

        ErrorCode errorCode = ex.getErrorCode();

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(errorCode.getCode());
        response.setMessage(errorCode.getMessage());

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    //lỗi validation
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<APIResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        APIResponse<Object> response = new APIResponse<>();

        response.setCode(ErrorCode.INVALID_INPUT.getCode());
        response.setMessage(ErrorCode.INVALID_INPUT.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
}
