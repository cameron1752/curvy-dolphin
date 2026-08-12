package com.biy.social.curvydolphin.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static com.biy.social.curvydolphin.exceptions.ErrorMessages.*;

@ControllerAdvice
@Slf4j
public class ErrorHandler {
    ErrorMessages errorMessages;

    @ExceptionHandler(UserException.class)
    private ResponseEntity<ApiError> UserException(UserException ex){
        ApiError error = new ApiError(USER_NOT_FOUND.getStatus(),
                USER_NOT_FOUND.getError_code(),
                USER_NOT_FOUND.getError(),
                ex.getMessage());
        return ResponseEntity.status(USER_NOT_FOUND.getStatus()).body(error);
    }

    @ExceptionHandler(VideoException.class)
    private ResponseEntity<ApiError> VideoException(VideoException ex) {
        ApiError error = new ApiError(VIDEO_NOT_FOUND.getStatus(),
                VIDEO_NOT_FOUND.getError_code(),
                VIDEO_NOT_FOUND.getError(),
                ex.getMessage());
        return ResponseEntity.status(VIDEO_NOT_FOUND.getStatus()).body(error);
    }

    @ExceptionHandler(CommentsException.class)
    private ResponseEntity<ApiError> CommentsException(CommentsException ex) {
        ApiError error = new ApiError(COMMENT_NOT_FOUND.getStatus(),
                COMMENT_NOT_FOUND.getError_code(),
                COMMENT_NOT_FOUND.getError(),
                ex.getMessage());
        return ResponseEntity.status(COMMENT_NOT_FOUND.getStatus()).body(error);
    }

    @ExceptionHandler(LikesException.class)
    private ResponseEntity<ApiError> LikesException(LikesException ex) {
        ApiError error = new ApiError(LIKE_NOT_FOUND.getStatus(),
                LIKE_NOT_FOUND.getError_code(),
                LIKE_NOT_FOUND.getError(),
                ex.getMessage());
        return ResponseEntity.status(LIKE_NOT_FOUND.getStatus()).body(error);
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
