package com.app.device_management.controller;

import com.app.device_management.dto.DeviceDto;
import com.app.device_management.service.DeviceService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("device")
public class DeviceController {

  @Autowired private DeviceService deviceService;

  @GetMapping
  public List<DeviceDto> getAllDevices() {
    return deviceService.getAllDevices();
  }

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto) {

    DeviceDto createdDevice = deviceService.createDevice(deviceDto);
    return ResponseEntity.created(URI.create("/device/v1/" + createdDevice.id()))
        .contentType(MediaType.APPLICATION_JSON)
        .body(createdDevice);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DeviceDto> updateDevice(
      @PathVariable Long id, @RequestBody DeviceDto deviceDto) {
    DeviceDto updatedDevice = deviceService.updateDevice(id, deviceDto);
    return ResponseEntity.ok(updatedDevice);
  }

  @GetMapping("/{id}")
  public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(deviceService.getDeviceById(id));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/brand/{brand}")
  public List<DeviceDto> getDevicesByBrand(@PathVariable String brand) {
    return deviceService.getDevicesByBrand(brand);
  }

  @GetMapping("/state/{state}")
  public List<DeviceDto> getDevicesByState(@PathVariable String state) {
    return deviceService.getDevicesByState(state);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
    deviceService.deleteDevice(id);
    return ResponseEntity.noContent().build();
  }
}
