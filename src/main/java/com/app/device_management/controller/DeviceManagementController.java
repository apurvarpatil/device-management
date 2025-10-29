package com.app.device_management.controller;

import com.app.device_management.dto.DeviceDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("device")
public class DeviceManagementController {
    private final Map<Long, DeviceDto> store = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @GetMapping
    public List<DeviceDto> getAllDevices() {
        return new ArrayList<>(store.values());
    }

    @PostMapping
    public ResponseEntity<DeviceDto> createDevice(@RequestBody DeviceDto deviceDto) {
        long id = idGenerator.incrementAndGet();

//        if(!Validator.validateRequest()) {
//           return
//        }
        DeviceDto device = DeviceDto.builder()
                .id(id)
                .name(deviceDto.name())
                .state(deviceDto.state())
                .brand(deviceDto.brand())
                .creationTime(LocalDateTime.now())
                .build();

        store.put(id, device);
        return ResponseEntity.created(URI.create("/device/v1" + id)).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeviceDto> updateDevice(
            @PathVariable Long id,
            @RequestBody DeviceDto deviceDto) {

        if (!store.containsKey(id)) {
            return ResponseEntity.notFound().build();
        }


        DeviceDto updatedDevice = DeviceDto.builder()
                .id(id)
                .name(deviceDto.name())
                .brand(deviceDto.brand())
                .state(deviceDto.state())
                .creationTime(store.get(id).creationTime())
                .build();

        store.put(id, updatedDevice);
        return ResponseEntity.ok(updatedDevice);
    }


}
