package com.spms.userservice.dto;

public record UserDTO(
    Long id,
    String username,
    String fullName,
    String email,
    String vehicleNumber,
    String vehicleType
) {}
