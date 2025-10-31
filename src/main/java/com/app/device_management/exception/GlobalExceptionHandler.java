package com.app.device_management.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DeviceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleException(DeviceNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(DeviceServiceException.class)
  public ResponseEntity<ErrorResponse> handleException(DeviceServiceException ex) {
    ErrorResponse error =
        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<Map<String, Object>> handleException(ValidationException ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("errors", ex.getErrors().getErrors());
    return ResponseEntity.badRequest().body(response);
  }

  record ErrorResponse(int status, String message) {}
}
