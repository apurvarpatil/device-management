package com.app.device_management.dto;

import com.app.device_management.dto.enums.StateEnum;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record DeviceDto(
    Long id, String name, String brand, StateEnum state, LocalDateTime creationTime) {}
