package com.lycheetech.movingapi.auth;

import com.lycheetech.movingapi.auth.dto.LoginRequest;
import com.lycheetech.movingapi.auth.dto.LoginResponse;
import com.lycheetech.movingapi.auth.dto.RegisterRequest;
import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.user.User;
import com.lycheetech.movingapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public LoginResponse login(LoginRequest request) {
        // TODO: integrate Spring Security / JWT
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Invalid credentials"));
        return LoginResponse.builder()
                .token("placeholder-token")
                .tokenType("Bearer")
                .email(request.getEmail())
                .build();
    }

    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already registered: " + request.getEmail());
        }
        if (userRepository.existsByFirebaseUid(request.getFirebaseUid())) {
            throw new BadRequestException("Firebase UID already registered: " + request.getFirebaseUid());
        }
        User user = User.builder()
                .firebaseUid(request.getFirebaseUid())
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .avatarUrl(request.getAvatarUrl())
                .build();
        userRepository.save(user);
    }
}

