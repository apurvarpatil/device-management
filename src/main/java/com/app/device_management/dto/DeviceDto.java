package com.app.device_management.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record DeviceDto(Long id, String name, String brand, StateEnum state, LocalDateTime creationTime) {

}
