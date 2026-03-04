package com.lycheetech.movingapi.invoice;

import com.lycheetech.movingapi.common.response.ApiResponse;
import com.lycheetech.movingapi.invoice.dto.InvoiceRequest;
import com.lycheetech.movingapi.invoice.dto.InvoiceResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getByBooking(@PathVariable UUID bookingId) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.findByBooking(bookingId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceResponse>> create(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Invoice created", invoiceService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<InvoiceResponse>> update(@PathVariable UUID id,
                                                               @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Invoice updated", invoiceService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        invoiceService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

