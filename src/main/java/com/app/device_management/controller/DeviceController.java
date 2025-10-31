package com.app.device_management.controller;

import com.app.device_management.dto.DeviceDto;
import com.app.device_management.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "Device API",
    description = "REST APIs capable of persisting and managing device resources.")
@RestController
@RequestMapping("device")
public class DeviceController {

  @Autowired private DeviceService deviceService;

  @Operation(summary = "Fetches the data of all devices")
  @GetMapping
  public List<DeviceDto> getAllDevices() {
    return deviceService.getAllDevices();
  }

  @Operation(summary = "Create a new device")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "Device created"),
    @ApiResponse(responseCode = "400", description = "Invalid input")
  })
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto) {

    DeviceDto createdDevice = deviceService.createDevice(deviceDto);
    return ResponseEntity.created(URI.create("/device/v1/" + createdDevice.id()))
        .contentType(MediaType.APPLICATION_JSON)
        .body(createdDevice);
  }

  @Operation(summary = "Update a device by id partially and/or fully.")
  @PutMapping("/{id}")
  public ResponseEntity<DeviceDto> updateDevice(
      @PathVariable Long id, @RequestBody DeviceDto deviceDto) {
    DeviceDto updatedDevice = deviceService.updateDevice(id, deviceDto);
    return ResponseEntity.ok(updatedDevice);
  }

  @Operation(summary = "Get a device by id")
  @GetMapping("/{id}")
  public ResponseEntity<DeviceDto> getDeviceById(@PathVariable Long id) {
    try {
      return ResponseEntity.ok(deviceService.getDeviceById(id));
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @Operation(summary = "List devices by brand")
  @GetMapping("/brand/{brand}")
  public List<DeviceDto> getDevicesByBrand(@PathVariable String brand) {
    return deviceService.getDevicesByBrand(brand);
  }

  @Operation(summary = "List devices by state")
  @GetMapping("/state/{state}")
  public List<DeviceDto> getDevicesByState(@PathVariable String state) {
    return deviceService.getDevicesByState(state);
  }

  @Operation(summary = "Delete a device by id")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
    deviceService.deleteDevice(id);
    return ResponseEntity.noContent().build();
  }
}
