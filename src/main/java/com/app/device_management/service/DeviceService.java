package com.app.device_management.service;

import com.app.device_management.dao.DeviceRepository;
import com.app.device_management.dto.DeviceDto;
import com.app.device_management.dto.enums.StateEnum;
import com.app.device_management.dto.enums.ValidationOperation;
import com.app.device_management.exception.DeviceNotFoundException;
import com.app.device_management.exception.DeviceServiceException;
import com.app.device_management.model.Device;
import com.app.device_management.validation.ValidationContext;
import com.app.device_management.validation.ValidationRule;
import jakarta.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class DeviceService {

  private final ValidationRule<Device> validationRule;

  private final DeviceRepository deviceRepository;

  public DeviceService(DeviceRepository deviceRepository, ValidationRule<Device> validationRule) {
    this.deviceRepository = deviceRepository;
    this.validationRule = validationRule;
  }

  public DeviceDto getDeviceById(Long id) {
    return deviceRepository
        .findById(id)
        .map(this::toDto)
        .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));
  }

  public List<DeviceDto> getAllDevices() {
    return deviceRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  public DeviceDto createDevice(DeviceDto deviceDto) {
    try {
      Device device = toEntity(deviceDto);
      Device savedDevice = deviceRepository.save(device);
      return toDto(savedDevice);
    } catch (Exception e) {
      throw new DeviceServiceException("Failed to create device", e);
    }
  }

  public DeviceDto updateDevice(Long id, DeviceDto deviceDto) {
    Device existingDevice =
        deviceRepository
            .findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

    Map<String, Boolean> changedFields = detectChangedFields(existingDevice, deviceDto);

    ValidationContext<Device> context =
        new ValidationContext<>(existingDevice, ValidationOperation.UPDATE, changedFields);

    validationRule.validate(context);

    if (null != deviceDto.name()) existingDevice.setName(deviceDto.name());
    if (null != deviceDto.brand()) existingDevice.setBrand(deviceDto.brand());
    if (null != deviceDto.state()) existingDevice.setState(deviceDto.state());

    return toDto(deviceRepository.save(existingDevice));
  }

  public List<DeviceDto> getDevicesByBrand(String brand) {
    return deviceRepository.findByBrand(brand).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public List<DeviceDto> getDevicesByState(String state) {
    return deviceRepository.findByState(StateEnum.valueOf(state)).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public void deleteDevice(Long id) {
    Device device =
        deviceRepository
            .findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Device not found with id: " + id));

    ValidationContext<Device> context =
        new ValidationContext<>(device, ValidationOperation.DELETE, new HashMap<>());

    validationRule.validate(context);
    deviceRepository.deleteById(id);
  }

  private Map<String, Boolean> detectChangedFields(Device existingDevice, DeviceDto newDto) {
    Map<String, Boolean> changedFields = new HashMap<>();

    changedFields.put(
        "name", newDto.name() != null && !newDto.name().equals(existingDevice.getName()));

    changedFields.put(
        "brand", newDto.brand() != null && !newDto.brand().equals(existingDevice.getBrand()));

    changedFields.put(
        "state", newDto.state() != null && !newDto.state().equals(existingDevice.getState()));

    changedFields.put(
        "creationTime",
        newDto.creationTime() != null
            && !newDto.creationTime().equals(existingDevice.getCreationTime()));

    return changedFields;
  }

  public DeviceDto toDto(Device device) {
    return DeviceDto.builder()
        .id(device.getId())
        .name(device.getName())
        .brand(device.getBrand())
        .state(device.getState())
        .creationTime(device.getCreationTime())
        .build();
  }

  private Device toEntity(DeviceDto deviceDto) {
    return Device.builder()
        .name(deviceDto.name())
        .brand(deviceDto.brand())
        .state(deviceDto.state())
        .build();
  }
}
