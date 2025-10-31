package com.app.device_management.service;

import com.app.device_management.dao.DeviceRepository;
import com.app.device_management.dto.DeviceDto;
import com.app.device_management.dto.enums.StateEnum;
import com.app.device_management.exception.DeviceNotFoundException;
import com.app.device_management.exception.ValidationException;
import com.app.device_management.model.Device;
import com.app.device_management.validation.ValidationContext;
import com.app.device_management.validation.ValidationRule;
import com.app.device_management.validation.rules.DeviceInUseValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @Mock
    private ValidationRule<Device> validationRule;

    @InjectMocks
    private DeviceService deviceService;

    private Device sampleDevice;

    @BeforeEach
    void setUp() {
        sampleDevice = Device.builder()
                .id(1L)
                .name("test")
                .brand("test-brand")
                .state(StateEnum.AVAILABLE)
                .build();
    }

    @Test
    void getAllDevices_returnsListFromRepository() {
        when(deviceRepository.findAll()).thenReturn(List.of(sampleDevice));

        var result = deviceService.getAllDevices();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void getDeviceById_found_returnsDevice() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));

        var result = deviceService.getDeviceById(1L);


        assertNotNull(result);
        assertEquals(deviceService.toDto(sampleDevice), result);
        verify(deviceRepository).findById(1L);
    }

    @Test
    void getDeviceById_notFound_throws() {
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> {
            deviceService.getDeviceById(99L);
        });

        verify(deviceRepository).findById(99L);
    }

    @Test
    void createDevice_savesAndReturns() {
        when(deviceRepository.save(any())).thenReturn(sampleDevice);

        var toCreate = DeviceDto.builder()
                .id(2L)
                .name("test-new")
                .brand("test-new-brand")
                .state(StateEnum.IN_USE)
                .build();
        var created = deviceService.createDevice(toCreate);

        assertNotNull(created);
        assertEquals(deviceService.toDto(sampleDevice), created);
        ArgumentCaptor<Device> captor = ArgumentCaptor.forClass(Device.class);
        verify(deviceRepository).save(captor.capture());
        assertEquals("test-new", captor.getValue().getName()); // adjust getter
    }

    @Test
    void updateDevice_existing_updatesAndSaves() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));
        when(deviceRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var updateDto = DeviceDto.builder()
                .name("updated-name")
                .brand(null)
                .state(StateEnum.IN_USE)
                .build();

        var result = deviceService.updateDevice(1L, updateDto);

        assertNotNull(result);
        assertEquals("updated-name", result.name());
        assertEquals(StateEnum.IN_USE, result.state());
        verify(deviceRepository).findById(1L);
        verify(deviceRepository).save(any(Device.class));
        verify(validationRule).validate(any());
    }

    @Test
    void updateDevice_brand_validation_fails_for_device_in_use() {

        Device existing = Device.builder()
                .id(1L)
                .name("test")
                .brand("test-brand")
                .state(StateEnum.IN_USE)
                .build();

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(existing));

        doAnswer(invocation -> {
            var ctx = invocation.getArgument(0);
            new DeviceInUseValidationRule()
                    .validate((ValidationContext<Device>) ctx);
            return null;
        }).when(validationRule).validate(any());

        var updateDto = DeviceDto.builder()
                .brand("new-brand")
                .state(StateEnum.IN_USE)
                .build();

        assertThrows(ValidationException.class,
                () -> deviceService.updateDevice(1L, updateDto));

        verify(deviceRepository).findById(1L);
        verify(deviceRepository, never()).save(any());
        verify(validationRule).validate(any());
    }


    @Test
    void updateDevice_notFound_throws() {
        when(deviceRepository.findById(99L)).thenReturn(Optional.empty());

        var updateDto = DeviceDto.builder()
                .name("test-device")
                .build();

        assertThrows(DeviceNotFoundException.class, () -> deviceService.updateDevice(99L, updateDto));
        verify(deviceRepository).findById(99L);
        verify(deviceRepository, never()).save(any());
    }

    @Test
    void deleteDevice_existing_deletes() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(sampleDevice));
        doNothing().when(deviceRepository).deleteById(1L);
        validationRule = new DeviceInUseValidationRule();


        deviceService.deleteDevice(1L);

        verify(deviceRepository).findById(1L);
        verify(deviceRepository).deleteById(1L);
    }


}
