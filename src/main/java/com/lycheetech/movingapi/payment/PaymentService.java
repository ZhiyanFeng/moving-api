package com.lycheetech.movingapi.payment;

import com.lycheetech.movingapi.move.MoveRepository;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.payment.dto.PaymentRequest;
import com.lycheetech.movingapi.payment.dto.PaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MoveRepository moveRepository;

    public List<PaymentResponse> findByMove(UUID moveId) {
        return paymentRepository.findByMoveId(moveId).stream().map(this::toResponse).toList();
    }

    public PaymentResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public PaymentResponse create(PaymentRequest request) {
        var move = moveRepository.findById(request.getMoveId())
                .orElseThrow(() -> new ResourceNotFoundException("Move not found: " + request.getMoveId()));

        Payment payment = Payment.builder()
                .move(move)
                .amount(request.getAmount())
                .build();
        if (request.getStripeStatus() != null) {
            payment.setStripeStatus(request.getStripeStatus());
        }
        if (request.getCurrency() != null) {
            payment.setCurrency(request.getCurrency());
        }
        payment.setStripePaymentIntentId(request.getStripePaymentIntentId());
        payment.setStripeInvoiceId(request.getStripeInvoiceId());
        payment.setStripeReceiptUrl(request.getStripeReceiptUrl());
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse update(UUID id, PaymentRequest request) {
        Payment payment = getOrThrow(id);
        if (request.getStripeStatus() != null) {
            payment.setStripeStatus(request.getStripeStatus());
        }
        payment.setStripePaymentIntentId(request.getStripePaymentIntentId());
        payment.setStripeInvoiceId(request.getStripeInvoiceId());
        payment.setStripeReceiptUrl(request.getStripeReceiptUrl());
        payment.setAmount(request.getAmount());
        if (request.getCurrency() != null) {
            payment.setCurrency(request.getCurrency());
        }
        return toResponse(paymentRepository.save(payment));
    }

    public void delete(UUID id) {
        paymentRepository.delete(getOrThrow(id));
    }

    private Payment getOrThrow(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + id));
    }

    private PaymentResponse toResponse(Payment p) {
        return PaymentResponse.builder()
                .id(p.getId())
                .moveId(p.getMove().getId())
                .stripeStatus(p.getStripeStatus())
                .stripePaymentIntentId(p.getStripePaymentIntentId())
                .stripeInvoiceId(p.getStripeInvoiceId())
                .stripeReceiptUrl(p.getStripeReceiptUrl())
                .amount(p.getAmount())
                .currency(p.getCurrency())
                .createdAt(p.getCreatedAt())
                .updatedAt(p.getUpdatedAt())
                .build();
    }
}

