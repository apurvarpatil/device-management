package com.app.device_management.validation;

import com.app.device_management.dto.ValidationOperation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ValidationContext<T> {
    private T newEntity;
    private T existingEntity;
    private ValidationOperation operation;
}