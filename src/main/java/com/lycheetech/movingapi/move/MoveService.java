package com.lycheetech.movingapi.move;

import com.lycheetech.movingapi.address.Address;
import com.lycheetech.movingapi.address.AddressRepository;
import com.lycheetech.movingapi.bid.BidRepository;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.move.dto.MoveRequest;
import com.lycheetech.movingapi.move.dto.MoveResponse;
import com.lycheetech.movingapi.mover.MoverRepository;
import com.lycheetech.movingapi.rfq.RfqRepository;
import com.lycheetech.movingapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final AddressRepository addressRepository;

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

    @Transactional
    public MoveResponse create(MoveRequest request) {
        var rfq = rfqRepository.findById(request.getRfqId())
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + request.getRfqId()));
        var mover = moverRepository.findById(request.getMoverId())
                .orElseThrow(() -> new ResourceNotFoundException("Mover not found: " + request.getMoverId()));
        var customer = userRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + request.getCustomerId()));
        var bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found: " + request.getBidId()));
        var status = moveStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Move status not found: " + request.getStatusId()));
        var pickupAddress = resolveAddress(request.getPickupAddressId());
        var dropoffAddress = resolveAddress(request.getDropoffAddressId());

        Move move = Move.builder()
                .rfq(rfq)
                .mover(mover)
                .customer(customer)
                .bid(bid)
                .status(status)
                .pickupAddress(pickupAddress)
                .dropoffAddress(dropoffAddress)
                .finalPickupAddressText(request.getFinalPickupAddressText())
                .finalDropoffAddressText(request.getFinalDropoffAddressText())
                .billedDistanceMeters(request.getBilledDistanceMeters())
                .finalPrice(request.getFinalPrice())
                .scheduledDate(request.getScheduledDate())
                .notes(request.getNotes())
                .build();
        Move savedMove = moveRepository.save(move);
        return toResponse(savedMove);
    }

    public MoveResponse update(UUID id, MoveRequest request) {
        Move move = getOrThrow(id);
        var status = moveStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Move status not found: " + request.getStatusId()));
        move.setStatus(status);
        move.setPickupAddress(resolveAddress(request.getPickupAddressId()));
        move.setDropoffAddress(resolveAddress(request.getDropoffAddressId()));
        move.setFinalPickupAddressText(request.getFinalPickupAddressText());
        move.setFinalDropoffAddressText(request.getFinalDropoffAddressText());
        move.setBilledDistanceMeters(request.getBilledDistanceMeters());
        move.setFinalPrice(request.getFinalPrice());
        move.setScheduledDate(request.getScheduledDate());
        move.setNotes(request.getNotes());
        return toResponse(moveRepository.save(move));
    }

    public void delete(UUID id) {
        moveRepository.delete(getOrThrow(id));
    }

    private Address resolveAddress(UUID addressId) {
        if (addressId == null) {
            return null;
        }
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + addressId));
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
                .status(m.getStatus().getName())
                .pickupAddressId(m.getPickupAddress() != null ? m.getPickupAddress().getId() : null)
                .dropoffAddressId(m.getDropoffAddress() != null ? m.getDropoffAddress().getId() : null)
                .finalPickupAddressText(m.getFinalPickupAddressText())
                .finalDropoffAddressText(m.getFinalDropoffAddressText())
                .billedDistanceMeters(m.getBilledDistanceMeters())
                .finalPrice(m.getFinalPrice())
                .scheduledDate(m.getScheduledDate())
                .notes(m.getNotes())
                .createdAt(m.getCreatedAt())
                .build();
    }
}

