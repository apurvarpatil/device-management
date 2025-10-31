package com.app.device_management.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseValidationRule<T> implements ValidationRule<T> {
  private ValidationRule<T> next;

  @Override
  public void setNext(ValidationRule<T> next) {
    this.next = next;
  }

  protected void validateNext(ValidationContext<T> context) {
    if (next != null) {
      next.validate(context);
    }
  }
}
