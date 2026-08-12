package com.biy.social.curvydolphin.exceptions;

public record ApiError(
        int status,
        int error_code,
        String error,
        String message) {
}
