package com.app.device_management.exception;

import com.app.device_management.validation.ValidationErrors;

public class ValidationException extends RuntimeException {
  private final ValidationErrors errors;

  public ValidationException(ValidationErrors errors) {
    super("Validation failed");
    this.errors = errors;
  }

  public ValidationException(String message, String field) {
    this(createSingleError(message, field));
  }

  private static ValidationErrors createSingleError(String message, String field) {
    ValidationErrors errors = new ValidationErrors();
    errors.addError(field, message);
    return errors;
  }

  public ValidationErrors getErrors() {
    return errors;
  }
}
