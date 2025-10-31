package com.app.device_management.validation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ValidationErrors {
  private final List<ValidationError> errors = new ArrayList<>();

  public void addError(String field, String message) {
    errors.add(new ValidationError(field, message));
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  public record ValidationError(String field, String message) {}
}
