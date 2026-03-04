package com.lycheetech.movingapi.payment;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.payment.dto.PaymentRequest;
import com.lycheetech.movingapi.payment.dto.PaymentResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.findByBooking(bookingId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(paymentService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Payment created", paymentService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Payment updated", paymentService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        paymentService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

