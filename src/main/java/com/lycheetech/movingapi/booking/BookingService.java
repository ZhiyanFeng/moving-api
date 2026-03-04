package com.lycheetech.movingapi.booking;

import com.lycheetech.movingapi.bid.BidRepository;
import com.lycheetech.movingapi.booking.dto.BookingRequest;
import com.lycheetech.movingapi.booking.dto.BookingResponse;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.mover.MoverRepository;
import com.lycheetech.movingapi.rfq.RfqRepository;
import com.lycheetech.movingapi.vehicle.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingStatusRepository bookingStatusRepository;
    private final RfqRepository rfqRepository;
    private final MoverRepository moverRepository;
    private final BidRepository bidRepository;
    private final VehicleRepository vehicleRepository;

    public List<BookingResponse> findAll() {
        return bookingRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<BookingResponse> findByMover(UUID moverId) {
        return bookingRepository.findByMoverId(moverId).stream().map(this::toResponse).toList();
    }

    public List<BookingResponse> findByUser(UUID userId) {
        return bookingRepository.findByRfqUserId(userId).stream().map(this::toResponse).toList();
    }

    public BookingResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public BookingResponse create(BookingRequest request) {
        var rfq = rfqRepository.findById(request.getRfqId())
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + request.getRfqId()));
        var mover = moverRepository.findById(request.getMoverId())
                .orElseThrow(() -> new ResourceNotFoundException("Mover not found: " + request.getMoverId()));
        var bid = bidRepository.findById(request.getBidId())
                .orElseThrow(() -> new ResourceNotFoundException("Bid not found: " + request.getBidId()));
        var vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found: " + request.getVehicleId()));
        var status = bookingStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking status not found: " + request.getStatusId()));

        Booking booking = Booking.builder()
                .rfq(rfq)
                .mover(mover)
                .bid(bid)
                .vehicle(vehicle)
                .status(status)
                .startDate(request.getStartDate())
                .completionDate(request.getCompletionDate())
                .notes(request.getNotes())
                .build();
        return toResponse(bookingRepository.save(booking));
    }

    public BookingResponse update(UUID id, BookingRequest request) {
        Booking booking = getOrThrow(id);
        var status = bookingStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking status not found: " + request.getStatusId()));
        booking.setStatus(status);
        booking.setStartDate(request.getStartDate());
        booking.setCompletionDate(request.getCompletionDate());
        booking.setNotes(request.getNotes());
        return toResponse(bookingRepository.save(booking));
    }

    public void delete(UUID id) {
        bookingRepository.delete(getOrThrow(id));
    }

    private Booking getOrThrow(UUID id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + id));
    }

    private BookingResponse toResponse(Booking b) {
        return BookingResponse.builder()
                .id(b.getId())
                .rfqId(b.getRfq().getId())
                .moverId(b.getMover().getId())
                .moverName(b.getMover().getName())
                .bidId(b.getBid().getId())
                .vehicleId(b.getVehicle().getId())
                .status(b.getStatus().getName())
                .startDate(b.getStartDate())
                .completionDate(b.getCompletionDate())
                .notes(b.getNotes())
                .createdAt(b.getCreatedAt())
                .build();
    }
}

