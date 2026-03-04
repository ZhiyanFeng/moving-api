package com.lycheetech.movingapi.inventory;

import com.lycheetech.movingapi.booking.BookingRepository;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.inventory.dto.InventoryRequest;
import com.lycheetech.movingapi.inventory.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final BookingRepository bookingRepository;

    public List<InventoryResponse> findByBooking(UUID bookingId) {
        return inventoryRepository.findByBookingId(bookingId).stream().map(this::toResponse).toList();
    }

    public InventoryResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public InventoryResponse create(InventoryRequest request) {
        var booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        Inventory inventory = Inventory.builder()
                .booking(booking)
                .name(request.getName())
                .quantity(request.getQuantity())
                .description(request.getDescription())
                .build();
        return toResponse(inventoryRepository.save(inventory));
    }

    public InventoryResponse update(UUID id, InventoryRequest request) {
        Inventory inventory = getOrThrow(id);
        inventory.setName(request.getName());
        inventory.setQuantity(request.getQuantity());
        inventory.setDescription(request.getDescription());
        return toResponse(inventoryRepository.save(inventory));
    }

    public void delete(UUID id) {
        inventoryRepository.delete(getOrThrow(id));
    }

    private Inventory getOrThrow(UUID id) {
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found: " + id));
    }

    private InventoryResponse toResponse(Inventory i) {
        return InventoryResponse.builder()
                .id(i.getId())
                .bookingId(i.getBooking().getId())
                .name(i.getName())
                .quantity(i.getQuantity())
                .description(i.getDescription())
                .build();
    }
}

