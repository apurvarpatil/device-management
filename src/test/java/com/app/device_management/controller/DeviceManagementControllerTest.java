package com.app.device_management.controller;

import com.app.device_management.dto.DeviceDto;
import com.app.device_management.dto.StateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private DeviceManagementController deviceManagementController;

    @Test
    void shouldCreateDevice() throws Exception {
        DeviceDto device = DeviceDto.builder()
                .name("test-device")
                .brand("test-brand")
                .creationTime(LocalDateTime.now())
                .state(StateEnum.AVAILABLE)
                .build();
//        when(deviceManagementController.createDevice(any())).thenReturn(ResponseEntity.created().build());

        mockMvc.perform(post("/device/v1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(device)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetAllDevices() throws Exception {
        List<DeviceDto> devices = List.of(
                DeviceDto.builder()
                        .id(123L)
                        .name("test-device1")
                        .brand("test-brand1")
                        .state(StateEnum.AVAILABLE)
                        .build(),
                DeviceDto.builder()
                        .id(124L)
                        .name("test-device2")
                        .brand("test-brand2")
                        .state(StateEnum.INACTIVE)
                        .build()

        );
        when(deviceManagementController.getAllDevices()).thenReturn(devices);

        mockMvc.perform(get("/device/v1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
}