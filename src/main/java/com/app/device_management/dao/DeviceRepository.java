package com.app.device_management.dao;

import com.app.device_management.dto.enums.StateEnum;
import com.app.device_management.model.Device;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
  List<Device> findByBrand(String brand);

  List<Device> findByState(StateEnum state);
}
