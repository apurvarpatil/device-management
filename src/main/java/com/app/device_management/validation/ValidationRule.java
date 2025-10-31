package com.app.device_management.validation;

import com.app.device_management.exception.ValidationException;

public interface ValidationRule<T> {
  void validate(ValidationContext<T> context) throws ValidationException;

  void setNext(ValidationRule<T> next);
}
