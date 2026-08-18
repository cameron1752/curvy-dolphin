package com.biy.social.curvydolphin.constants;

import org.springframework.http.HttpStatus;

public enum ErrorMessages {

    USER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            1001,
            "USER_NOT_FOUND"
    ),
    VIDEO_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            1002,
            "VIDEO_NOT_FOUND"
    ),
    COMMENT_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            1003,
            "COMMENT_NOT_FOUND"
    ),
    LIKE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            1004,
            "LIKE_NOT_FOUND"
    ),
    VIDEO_INTERACTION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            1005,
            "VIDEO_INTERACTION_NOT_FOUND"
    ),
    MISSING_REQUEST_HEADER(
            HttpStatus.BAD_REQUEST,
            1006,
            "MISSING_REQUEST_HEADER"
    ),
    AUTHORIZATION_INVALID(
            HttpStatus.UNAUTHORIZED,
            1007,
            "AUTHORIZATION_INVALID"
    );

    private final HttpStatus status;
    private final int error_code;
    private final String error;

    ErrorMessages(HttpStatus status, int error_code, String error) {
        this.status = status;
        this.error_code = error_code;
        this.error = error;
    }

    public int getStatus() {
        return status.value();
    }

    public int getError_code() {
        return error_code;
    }

    public String getError(){
        return error;
    }

}