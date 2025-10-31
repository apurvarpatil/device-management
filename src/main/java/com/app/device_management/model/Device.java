package com.app.device_management.model;

import com.app.device_management.dto.enums.StateEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "devices")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Device {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String brand;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private StateEnum state;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime creationTime;
}
