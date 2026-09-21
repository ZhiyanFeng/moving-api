package com.lycheetech.movingapi.user;

import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.user.dto.UserRequest;
import com.lycheetech.movingapi.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public UserResponse findByFirebaseUid(String firebaseUid) {
        return toResponse(userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for firebase uid: " + firebaseUid)));
    }

    public UserResponse create(String firebaseUid, UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already in use: " + request.getEmail());
        }
        if (userRepository.existsByFirebaseUid(firebaseUid)) {
            throw new BadRequestException("Firebase UID already in use: " + firebaseUid);
        }
        User user = User.builder()
                .firebaseUid(firebaseUid)
                .email(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .avatarUrl(request.getAvatarUrl())
                .build();
        return toResponse(userRepository.save(user));
    }

    public UserResponse update(UUID id, UserRequest request) {
        User user = getOrThrow(id);
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAvatarUrl(request.getAvatarUrl());
        return toResponse(userRepository.save(user));
    }

    public void delete(UUID id) {
        userRepository.delete(getOrThrow(id));
    }

    private User getOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .firebaseUid(user.getFirebaseUid())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .avatarUrl(user.getAvatarUrl())
                .ratingAvg(user.getRatingAvg())
                .completedMoves(user.getCompletedMoves())
                .canceledMoves(user.getCanceledMoves())
                .createdAt(user.getCreatedAt())
                .build();
    }
}

