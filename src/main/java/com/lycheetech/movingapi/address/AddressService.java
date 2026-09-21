package com.lycheetech.movingapi.address;

import com.lycheetech.movingapi.address.dto.AddressRequest;
import com.lycheetech.movingapi.address.dto.AddressResponse;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.user.User;
import com.lycheetech.movingapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    public List<AddressResponse> findByUser(UUID userId) {
        return addressRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .toList();
    }

    public AddressResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public AddressResponse create(AddressRequest request) {
        User user = resolveUser(request.getUserId());

        Address address = Address.builder()
                .user(user)
                .googlePlaceId(request.getGooglePlaceId())
                .formattedAddress(request.getFormattedAddress())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .postalCode(request.getPostalCode())
                .city(request.getCity())
                .stateProvince(request.getStateProvince())
                .country(request.getCountry())
                .label(request.getLabel())
                .unitApartmentNumber(request.getUnitApartmentNumber())
                .hasElevator(request.getHasElevator() != null && request.getHasElevator())
                .parkingRestrictions(request.getParkingRestrictions())
                .build();
        return toResponse(addressRepository.save(address));
    }

    @Transactional
    public AddressResponse update(UUID id, AddressRequest request) {
        Address address = getOrThrow(id);
        address.setUser(resolveUser(request.getUserId()));
        address.setGooglePlaceId(request.getGooglePlaceId());
        address.setFormattedAddress(request.getFormattedAddress());
        address.setLatitude(request.getLatitude());
        address.setLongitude(request.getLongitude());
        address.setPostalCode(request.getPostalCode());
        address.setCity(request.getCity());
        address.setStateProvince(request.getStateProvince());
        address.setCountry(request.getCountry());
        address.setLabel(request.getLabel());
        address.setUnitApartmentNumber(request.getUnitApartmentNumber());
        address.setHasElevator(request.getHasElevator() != null && request.getHasElevator());
        address.setParkingRestrictions(request.getParkingRestrictions());
        return toResponse(addressRepository.save(address));
    }

    @Transactional
    public void delete(UUID id) {
        addressRepository.delete(getOrThrow(id));
    }

    private User resolveUser(UUID userId) {
        if (userId == null) {
            return null;
        }
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
    }

    private Address getOrThrow(UUID id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Address not found: " + id));
    }

    private AddressResponse toResponse(Address a) {
        return AddressResponse.builder()
                .id(a.getId())
                .userId(a.getUser() != null ? a.getUser().getId() : null)
                .googlePlaceId(a.getGooglePlaceId())
                .formattedAddress(a.getFormattedAddress())
                .latitude(a.getLatitude())
                .longitude(a.getLongitude())
                .postalCode(a.getPostalCode())
                .city(a.getCity())
                .stateProvince(a.getStateProvince())
                .country(a.getCountry())
                .label(a.getLabel())
                .unitApartmentNumber(a.getUnitApartmentNumber())
                .hasElevator(a.getHasElevator())
                .parkingRestrictions(a.getParkingRestrictions())
                .createdAt(a.getCreatedAt())
                .updatedAt(a.getUpdatedAt())
                .build();
    }
}

