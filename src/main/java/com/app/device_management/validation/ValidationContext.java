package com.app.device_management.validation;

import com.app.device_management.dto.enums.ValidationOperation;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationContext<T> {
  private T entity;
  private ValidationOperation operation;
  private final Map<String, Boolean> changedFields;

  public ValidationContext(
      T entity, ValidationOperation operation, Map<String, Boolean> changedFields) {
    this.entity = entity;
    this.operation = operation;
    this.changedFields = changedFields;
  }

  public boolean hasFieldChanged(String fieldName) {
    return changedFields.getOrDefault(fieldName, false);
  }
}
