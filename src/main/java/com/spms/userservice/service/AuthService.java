package com.spms.userservice.service;

import com.spms.userservice.dto.AuthResponse;
import com.spms.userservice.dto.LoginRequest;
import com.spms.userservice.dto.RegisterRequest;
import com.spms.userservice.entity.User;
import com.spms.userservice.repository.UserRepository;
import com.spms.userservice.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            return new AuthResponse(false, "Username already exists", null, null, null);
        }
        if (userRepository.existsByEmail(request.email())) {
            return new AuthResponse(false, "Email already exists", null, null, null);
        }

        User user = new User(
                null,
                request.username(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.fullName(),
                request.vehicleNumber(),
                request.vehicleType(),
                null
        );

        User savedUser = userRepository.save(user);
        String token = jwtProvider.generateToken(savedUser.getUsername());

        return new AuthResponse(true, "Registration Successful", savedUser.getId(), savedUser.getEmail(), token);
    }

    public AuthResponse login(LoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .map(user -> {
                    String token = jwtProvider.generateToken(user.getUsername());
                    return new AuthResponse(true, "Login Successful", user.getId(), user.getEmail(), token);
                })
                .orElse(new AuthResponse(false, "Invalid credentials", null, null, null));
    }
}
