package com.biy.social.curvydolphin.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.biy.social.curvydolphin.exceptions.ErrorMessages.MISSING_REQUEST_HEADER;
import static com.biy.social.curvydolphin.exceptions.ErrorMessages.USER_NOT_FOUND;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
    ErrorMessages errorMessages;

    @ExceptionHandler(UserException.class)
    private ResponseEntity<ApiError> handleCurvyDolphinException(UserException ex){
        ApiError error = new ApiError(USER_NOT_FOUND.getStatus(),
                USER_NOT_FOUND.getError_code(),
                USER_NOT_FOUND.getError(),
                ex.getMessage());
        return ResponseEntity.status(USER_NOT_FOUND.getStatus()).body(error);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ApiError> handleMissingRequestHeaderException(MissingRequestHeaderException ex){
        log.error("Missing request header. {} is required", ex.getHeaderName());
        ApiError error = new ApiError(MISSING_REQUEST_HEADER.getStatus(),
                MISSING_REQUEST_HEADER.getError_code(),
                MISSING_REQUEST_HEADER.getError(),
                "Missing request header, " + ex.getHeaderName() + " is required!");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
