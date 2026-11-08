package com.lycheetech.movingapi.bid;

import com.lycheetech.movingapi.bid.dto.BidRequest;
import com.lycheetech.movingapi.bid.dto.BidResponse;
import com.lycheetech.movingapi.common.exception.BadRequestException;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.mover.MoverRepository;
import com.lycheetech.movingapi.rfq.RfqRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidRepository bidRepository;
    private final BidStatusRepository bidStatusRepository;
    private final MoverRepository moverRepository;
    private final RfqRepository rfqRepository;

    public List<BidResponse> findAll() {
        return bidRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<BidResponse> findByRfq(UUID rfqId) {
        return bidRepository.findByRfqId(rfqId).stream().map(this::toResponse).toList();
    }

    public List<BidResponse> findByMover(UUID moverId) {
        return bidRepository.findByMoverId(moverId).stream().map(this::toResponse).toList();
    }

    public BidResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public BidResponse create(BidRequest request) {
        var mover = moverRepository.findById(request.getMoverId())
                .orElseThrow(() -> new ResourceNotFoundException("Mover not found: " + request.getMoverId()));
        var rfq = rfqRepository.findById(request.getRfqId())
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + request.getRfqId()));
        var status = bidStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid status not found: " + request.getStatusId()));

        Integer minHours = normalizeMinHours(request.getPricingType(), request.getMinHours());

        Bid bid = Bid.builder()
                .mover(mover)
                .rfq(rfq)
                .status(status)
                .pricingType(request.getPricingType())
                .price(request.getPrice())
                .minHours(minHours)
                .details(request.getDetails())
                .build();
        return toResponse(bidRepository.save(bid));
    }

    public BidResponse update(UUID id, BidRequest request) {
        Bid bid = getOrThrow(id);
        var status = bidStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid status not found: " + request.getStatusId()));

        Integer minHours = normalizeMinHours(request.getPricingType(), request.getMinHours());

        bid.setStatus(status);
        bid.setPricingType(request.getPricingType());
        bid.setPrice(request.getPrice());
        bid.setMinHours(minHours);
        bid.setDetails(request.getDetails());
        return toResponse(bidRepository.save(bid));
    }

    public void delete(UUID id) {
        bidRepository.delete(getOrThrow(id));
    }

    private Bid getOrThrow(UUID id) {
        return bidRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found: " + id));
    }

    // Mirrors the DB's check_hourly_minimum constraint at the application
    // layer so clients get a clean 400 instead of a raw constraint-violation
    // error, and so FIXED bids always get minHours normalized to 0.
    private Integer normalizeMinHours(PricingType pricingType, Integer minHours) {
        if (pricingType == PricingType.FIXED) {
            if (minHours != null && minHours != 0) {
                throw new BadRequestException("minHours must be 0 (or omitted) when pricingType is FIXED");
            }
            return 0;
        }
        // HOURLY
        return minHours != null ? minHours : 0;
    }

    private BidResponse toResponse(Bid bid) {
        return BidResponse.builder()
                .id(bid.getId())
                .moverId(bid.getMover().getId())
                .moverName(bid.getMover().getName())
                .rfqId(bid.getRfq().getId())
                .status(bid.getStatus().getName())
                .pricingType(bid.getPricingType())
                .price(bid.getPrice())
                .minHours(bid.getMinHours())
                .details(bid.getDetails())
                .createdAt(bid.getCreatedAt())
                .build();
    }
}

