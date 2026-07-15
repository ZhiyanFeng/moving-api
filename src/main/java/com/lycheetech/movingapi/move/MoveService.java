package com.lycheetech.movingapi.move;

import com.lycheetech.movingapi.bid.BidRepository;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.move.dto.MoveRequest;
import com.lycheetech.movingapi.move.dto.MoveResponse;
import com.lycheetech.movingapi.mover.MoverRepository;
import com.lycheetech.movingapi.rfq.RfqRepository;
import com.lycheetech.movingapi.user.UserRepository;
import com.lycheetech.movingapi.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MoveService {

    private final MoveRepository moveRepository;
    private final MoveStatusRepository moveStatusRepository;
    private final RfqRepository rfqRepository;
    private final MoverRepository moverRepository;
    private final UserRepository userRepository;
    private final BidRepository bidRepository;
    private final VehicleRepository vehicleRepository;

    public List<MoveResponse> findAll() {
        return moveRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<MoveResponse> findByMover(UUID moverId) {
        return moveRepository.findByMoverId(moverId).stream().map(this::toResponse).toList();
    }

    public List<MoveResponse> findByUser(UUID userId) {
        return moveRepository.findByRfqUserId(userId).stream().map(this::toResponse).toList();
    }

    public MoveResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public MoveResponse create(MoveRequest request) {
        var rfq = rfqRepository.findById(request.getRfqId())
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + request.getRfqId()));
        var mover = moverRepository.findById(request.getMoverId())
                .orElseThrow(() -> new ResourceNotFoundException("Mover not found: " + request.getMoverId()));
        var customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));
        var bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found: " + request.getBidId()));
        var vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVehicleId()));
        var status = moveStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Move status not found: " + request.getStatusId()));

        Move move = Move.builder()
                .rfq(rfq)
                .mover(mover)
                .customer(customer)
                .bid(bid)
                .vehicle(vehicle)
                .status(status)
                .startDate(request.getStartDate())
                .completionDate(request.getCompletionDate())
                .notes(request.getNotes())
                .build();
        return toResponse(moveRepository.save(move));
    }

    public MoveResponse update(UUID id, MoveRequest request) {
        Move move = getOrThrow(id);
        var status = moveStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Move status not found: " + request.getStatusId()));
        move.setStatus(status);
        move.setStartDate(request.getStartDate());
        move.setCompletionDate(request.getCompletionDate());
        move.setNotes(request.getNotes());
        return toResponse(moveRepository.save(move));
    }

    public void delete(UUID id) {
        moveRepository.delete(getOrThrow(id));
    }

    private Move getOrThrow(UUID id) {
        return moveRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Move not found: " + id));
    }

    private MoveResponse toResponse(Move m) {
        return MoveResponse.builder()
                .id(m.getId())
                .rfqId(m.getRfq().getId())
                .moverId(m.getMover().getId())
                .moverName(m.getMover().getName())
                .customerId(m.getCustomer().getId())
                .bidId(m.getBid().getId())
                .vehicleId(m.getVehicle().getId())
                .status(m.getStatus().getName())
                .startDate(m.getStartDate())
                .completionDate(m.getCompletionDate())
                .notes(m.getNotes())
                .createdAt(m.getCreatedAt())
                .build();
    }
}

