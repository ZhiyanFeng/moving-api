package com.lycheetech.movingapi.rfq;

import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.lookup.movingservice.MovingServiceRepository;
import com.lycheetech.movingapi.rfq.dto.RfqRequest;
import com.lycheetech.movingapi.rfq.dto.RfqResponse;
import com.lycheetech.movingapi.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RfqService {

    private final RfqRepository rfqRepository;
    private final RfqStatusRepository rfqStatusRepository;
    private final UserRepository userRepository;
    private final MovingServiceRepository movingServiceRepository;

    public List<RfqResponse> findAll() {
        return rfqRepository.findAll().stream().map(this::toResponse).toList();
    }

    public List<RfqResponse> findByUser(UUID userId) {
        return rfqRepository.findByUserId(userId).stream().map(this::toResponse).toList();
    }

    public RfqResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public RfqResponse create(String firebaseUid, RfqRequest request) {
        var user = userRepository.findByFirebaseUid(firebaseUid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found for firebase uid: " + firebaseUid));
        // A brand-new RFQ is always OPEN - status is system-controlled, not
        // client-supplied, so request.getStatusId() is ignored on create.
        var status = rfqStatusRepository.findByName("OPEN")
                .orElseThrow(() -> new ResourceNotFoundException("RFQ status not found: OPEN"));
        var service = request.getServiceId() != null
                ? movingServiceRepository.findById(request.getServiceId()).orElse(null)
                : null;

        Rfq rfq = Rfq.builder()
                .user(user)
                .service(service)
                .status(status)
                .originAddress(request.getOriginAddress())
                .destinationAddress(request.getDestinationAddress())
                .moveDate(request.getMoveDate())
                .notes(request.getNotes())
                .build();

        Rfq savedRfq = rfqRepository.save(rfq);

        return toResponse(savedRfq);
    }

    @Transactional
    public RfqResponse update(UUID id, RfqRequest request) {
        Rfq rfq = getOrThrow(id);
        // Status transitions (OPEN -> CLOSED/CANCELLED) are optional here;
        // only change it if the caller explicitly sent a statusId.
        if (request.getStatusId() != null) {
            var status = rfqStatusRepository.findById(request.getStatusId())
                    .orElseThrow(() -> new ResourceNotFoundException("RFQ status not found: " + request.getStatusId()));
            rfq.setStatus(status);
        }
        var service = request.getServiceId() != null
                ? movingServiceRepository.findById(request.getServiceId()).orElse(null)
                : null;
        rfq.setService(service);
        rfq.setOriginAddress(request.getOriginAddress());
        rfq.setDestinationAddress(request.getDestinationAddress());
        rfq.setMoveDate(request.getMoveDate());
        rfq.setNotes(request.getNotes());
        if (request.getSpecialtyItems() != null) {
            rfq.setSpecialtyItems(request.getSpecialtyItems());
        }
        Rfq savedRfq = rfqRepository.save(rfq);

        return toResponse(savedRfq);
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
                .userName(rfq.getUser().getFirstName() + " " + rfq.getUser().getLastName())
                .serviceId(rfq.getService() != null ? rfq.getService().getId() : null)
                .serviceName(rfq.getService() != null ? rfq.getService().getName() : null)
                .status(rfq.getStatus().getName())
                .originAddress(rfq.getOriginAddress())
                .destinationAddress(rfq.getDestinationAddress())
                .moveDate(rfq.getMoveDate())
                .notes(rfq.getNotes())
                .specialtyItems(rfq.getSpecialtyItems())
                .createdAt(rfq.getCreatedAt())
                .build();
    }
}



