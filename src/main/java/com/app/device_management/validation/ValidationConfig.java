package com.app.device_management.validation;

import com.app.device_management.model.Device;
import com.app.device_management.validation.rules.DeviceInUseValidationRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidationConfig {

  @Bean
  public ValidationRule<Device> deviceValidationRule() {
    return new DeviceInUseValidationRule();
  }
}
