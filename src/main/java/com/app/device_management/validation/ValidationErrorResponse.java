package com.app.device_management.validation;

import java.time.LocalDateTime;

public record ValidationErrorResponse(
    int status, String message, String field, LocalDateTime timestamp) {}
