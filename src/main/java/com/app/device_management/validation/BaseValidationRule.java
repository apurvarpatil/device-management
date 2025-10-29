package com.app.device_management.validation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseValidationRule<T> implements ValidationRule<T> {
    private ValidationRule<T> next;

    @Override
    public void validate(ValidationContext<T> context) {

        doValidate(context);
        if (next != null) {
            next.validate(context);
        }
    }

    protected abstract void doValidate(ValidationContext<T> context);
}
