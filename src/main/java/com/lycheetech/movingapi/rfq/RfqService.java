package com.lycheetech.movingapi.rfq;

import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.lookup.businessservice.BusinessServiceRepository;
import com.lycheetech.movingapi.rfq.dto.RfqRequest;
import com.lycheetech.movingapi.rfq.dto.RfqResponse;
import com.lycheetech.movingapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RfqService {

    private final RfqRepository rfqRepository;
    private final RfqStatusRepository rfqStatusRepository;
    private final UserRepository userRepository;
    private final BusinessServiceRepository businessServiceRepository;

    public List<RfqResponse> findAll() {
        return rfqRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<RfqResponse> findByUser(UUID userId) {
        return rfqRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public RfqResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public RfqResponse create(RfqRequest request) {
        var user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));
        var status = rfqStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("RFQ status not found: " + request.getStatusId()));
        var service = request.getServiceId() != null
                ? businessServiceRepository.findById(request.getServiceId()).orElse(null)
                : null;

        Rfq rfq = Rfq.builder()
                .user(user)
                .service(service)
                .status(status)
                .originAddress(request.getOriginAddress())
                .destinationAddress(request.getDestinationAddress())
                .moveDate(request.getMoveDate())
                .description(request.getDescription())
                .itemDetails(request.getItemDetails())
                .build();
        return toResponse(rfqRepository.save(rfq));
    }

    public RfqResponse update(UUID id, RfqRequest request) {
        Rfq rfq = getOrThrow(id);
        var status = rfqStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("RFQ status not found: " + request.getStatusId()));
        var service = request.getServiceId() != null
                ? businessServiceRepository.findById(request.getServiceId()).orElse(null)
                : null;
        rfq.setService(service);
        rfq.setStatus(status);
        rfq.setOriginAddress(request.getOriginAddress());
        rfq.setDestinationAddress(request.getDestinationAddress());
        rfq.setMoveDate(request.getMoveDate());
        rfq.setDescription(request.getDescription());
        rfq.setItemDetails(request.getItemDetails());
        return toResponse(rfqRepository.save(rfq));
    }

    public void delete(UUID id) {
        rfqRepository.delete(getOrThrow(id));
    }

    private Rfq getOrThrow(UUID id) {
        return rfqRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RFQ not found: " + id));
    }

    private RfqResponse toResponse(Rfq rfq) {
        return RfqResponse.builder()
                .id(rfq.getId())
                .userId(rfq.getUser().getId())
                .userName(rfq.getUser().getName())
                .serviceId(rfq.getService() != null ? rfq.getService().getId() : null)
                .serviceName(rfq.getService() != null ? rfq.getService().getName() : null)
                .status(rfq.getStatus().getName())
                .originAddress(rfq.getOriginAddress())
                .destinationAddress(rfq.getDestinationAddress())
                .moveDate(rfq.getMoveDate())
                .description(rfq.getDescription())
                .itemDetails(rfq.getItemDetails())
                .createdAt(rfq.getCreatedAt())
                .build();
    }
}

