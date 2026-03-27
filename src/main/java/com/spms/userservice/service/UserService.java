package com.spms.userservice.service;

import com.spms.userservice.dto.UserDTO;
import com.spms.userservice.entity.User;
import com.spms.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserDTO getProfile(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getVehicleNumber(),
                user.getVehicleType()
        );
    }
}
