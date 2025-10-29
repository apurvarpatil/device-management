package com.app.device_management.validation;

public interface ValidationRule<T> {
    void validate(ValidationContext<T> context);
    void setNext(ValidationRule<T> next);
}
