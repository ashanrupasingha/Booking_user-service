package com.spms.userservice.dto;

public record AuthResponse(boolean success, String message, Long userId, String email, String token) {}
