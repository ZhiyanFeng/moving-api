package com.lycheetech.movingapi.booking;

import com.lycheetech.movingapi.booking.dto.BookingRequest;
import com.lycheetech.movingapi.booking.dto.BookingResponse;
import com.lycheetech.movingapi.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findAll()));
    }

    @GetMapping("/mover/{moverId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getByMover(@PathVariable UUID moverId) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findByMover(moverId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BookingResponse>>> getByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findByUser(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> create(@Valid @RequestBody BookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Booking created", bookingService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookingResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody BookingRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Booking updated", bookingService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        bookingService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

