package com.lycheetech.movingapi.payment;

import com.lycheetech.movingapi.booking.BookingRepository;
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
    private final PaymentStatusRepository paymentStatusRepository;
    private final BookingRepository bookingRepository;

    public List<PaymentResponse> findByBooking(UUID bookingId) {
        return paymentRepository.findByBookingId(bookingId).stream().map(this::toResponse).toList();
    }

    public PaymentResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public PaymentResponse create(PaymentRequest request) {
        var booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        var status = paymentStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment status not found: " + request.getStatusId()));

        Payment payment = Payment.builder()
                .booking(booking)
                .status(status)
                .amount(request.getAmount())
                .paymentType(request.getPaymentType())
                .transactionId(request.getTransactionId())
                .build();
        return toResponse(paymentRepository.save(payment));
    }

    public PaymentResponse update(UUID id, PaymentRequest request) {
        Payment payment = getOrThrow(id);
        var status = paymentStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Payment status not found: " + request.getStatusId()));
        payment.setStatus(status);
        payment.setAmount(request.getAmount());
        payment.setPaymentType(request.getPaymentType());
        payment.setTransactionId(request.getTransactionId());
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
                .bookingId(p.getBooking().getId())
                .status(p.getStatus().getName())
                .amount(p.getAmount())
                .paymentType(p.getPaymentType())
                .transactionId(p.getTransactionId())
                .paymentDate(p.getCreatedAt())
                .build();
    }
}

