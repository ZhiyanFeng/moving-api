package com.lycheetech.movingapi.bid;

import com.lycheetech.movingapi.bid.dto.BidRequest;
import com.lycheetech.movingapi.bid.dto.BidResponse;
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

        Bid bid = Bid.builder()
                .mover(mover)
                .rfq(rfq)
                .status(status)
                .amount(request.getAmount())
                .details(request.getDetails())
                .build();
        return toResponse(bidRepository.save(bid));
    }

    public BidResponse update(UUID id, BidRequest request) {
        Bid bid = getOrThrow(id);
        var status = bidStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid status not found: " + request.getStatusId()));
        bid.setStatus(status);
        bid.setAmount(request.getAmount());
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

    private BidResponse toResponse(Bid bid) {
        return BidResponse.builder()
                .id(bid.getId())
                .moverId(bid.getMover().getId())
                .moverName(bid.getMover().getName())
                .rfqId(bid.getRfq().getId())
                .status(bid.getStatus().getName())
                .amount(bid.getAmount())
                .details(bid.getDetails())
                .createdAt(bid.getCreatedAt())
                .build();
    }
}

