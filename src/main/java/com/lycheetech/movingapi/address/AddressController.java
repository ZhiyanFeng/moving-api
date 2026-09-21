package com.lycheetech.movingapi.address;

import com.lycheetech.movingapi.address.dto.AddressRequest;
import com.lycheetech.movingapi.address.dto.AddressResponse;
import com.lycheetech.movingapi.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(addressService.findByUser(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(addressService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AddressResponse>> create(@Valid @RequestBody AddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Address created", addressService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AddressResponse>> update(@PathVariable UUID id,
                                                                @Valid @RequestBody AddressRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Address updated", addressService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        addressService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

