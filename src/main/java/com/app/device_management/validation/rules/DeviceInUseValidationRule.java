package com.app.device_management.validation.rules;

import com.app.device_management.dto.enums.StateEnum;
import com.app.device_management.dto.enums.ValidationOperation;
import com.app.device_management.exception.ValidationException;
import com.app.device_management.model.Device;
import com.app.device_management.validation.BaseValidationRule;
import com.app.device_management.validation.ValidationContext;
import com.app.device_management.validation.ValidationErrors;
import java.util.Map;

public class DeviceInUseValidationRule extends BaseValidationRule<Device> {

  @Override
  public void validate(ValidationContext<Device> context) throws ValidationException {
    Device device = context.getEntity();
    Map<String, Boolean> changes = context.getChangedFields();
    ValidationErrors errors = new ValidationErrors();

    if (context.getOperation() == ValidationOperation.UPDATE) {

      if (changes.get("creationTime")) {
        errors.addError("creationTime", "Creation time cannot be updated");
      }

      if (device.getState() == StateEnum.IN_USE) {
        if (changes.get("name")) {
          errors.addError("name", "Device name cannot be updated while device is in use");
        }
        if (changes.get("brand")) {
          errors.addError("brand", "Device brand cannot be updated while device is in use");
        }
      }
    } else if (context.getOperation() == ValidationOperation.DELETE
        && device.getState() == StateEnum.IN_USE) {
      errors.addError("state", "Device cannot be deleted while in use");
    }

    if (errors.hasErrors()) {
      throw new ValidationException(errors);
    }

    validateNext(context);
  }
}
