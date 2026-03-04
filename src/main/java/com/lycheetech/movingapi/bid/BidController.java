package com.lycheetech.movingapi.bid;

import com.lycheetech.movingapi.bid.dto.BidRequest;
import com.lycheetech.movingapi.bid.dto.BidResponse;
import com.lycheetech.movingapi.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bids")
@RequiredArgsConstructor
public class BidController {

    private final BidService bidService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<BidResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(bidService.findAll()));
    }

    @GetMapping("/rfq/{rfqId}")
    public ResponseEntity<ApiResponse<List<BidResponse>>> getByRfq(@PathVariable UUID rfqId) {
        return ResponseEntity.ok(ApiResponse.success(bidService.findByRfq(rfqId)));
    }

    @GetMapping("/mover/{moverId}")
    public ResponseEntity<ApiResponse<List<BidResponse>>> getByMover(@PathVariable UUID moverId) {
        return ResponseEntity.ok(ApiResponse.success(bidService.findByMover(moverId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(bidService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<BidResponse>> create(@Valid @RequestBody BidRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Bid created", bidService.create(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BidResponse>> update(@PathVariable UUID id,
                                                           @Valid @RequestBody BidRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Bid updated", bidService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        bidService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

