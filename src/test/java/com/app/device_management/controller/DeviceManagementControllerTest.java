package com.app.device_management.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.device_management.dto.DeviceDto;
import com.app.device_management.dto.enums.StateEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class DeviceManagementControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Mock private DeviceController deviceManagementController;

  void shouldCreateDevice() throws Exception {
    DeviceDto device =
        DeviceDto.builder()
            .name("test-device")
            .brand("test-brand")
            .creationTime(LocalDateTime.now())
            .state(StateEnum.AVAILABLE)
            .build();

    mockMvc
        .perform(
            post("/device/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(device)))
        .andExpect(status().isCreated());
  }

  void shouldGetAllDevices() throws Exception {
    List<DeviceDto> devices =
        List.of(
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
                .build());
    when(deviceManagementController.getAllDevices()).thenReturn(devices);

    mockMvc
        .perform(get("/device/v1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray());
  }
}
