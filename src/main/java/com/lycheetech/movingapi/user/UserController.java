package com.lycheetech.movingapi.user;

import com.lycheetech.movingapi.auth.firebase.FirebaseUserPrincipal;
import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.user.dto.UserRequest;
import com.lycheetech.movingapi.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(userService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(userService.findById(id)));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> me(@AuthenticationPrincipal FirebaseUserPrincipal principal) {
        String firebaseUid = principal.getUid();
        return ResponseEntity.ok(ApiResponse.success(userService.findByFirebaseUid(firebaseUid)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> create(@AuthenticationPrincipal FirebaseUserPrincipal principal,
                                                             @Valid @RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created", userService.create(principal.getUid(), request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(@PathVariable UUID id,
                                                            @Valid @RequestBody UserRequest request) {
        return ResponseEntity.ok(ApiResponse.success("User updated", userService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

