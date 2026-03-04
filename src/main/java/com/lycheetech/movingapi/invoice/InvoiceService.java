package com.lycheetech.movingapi.invoice;

import com.lycheetech.movingapi.booking.BookingRepository;
import com.lycheetech.movingapi.common.exception.ResourceNotFoundException;
import com.lycheetech.movingapi.invoice.dto.InvoiceRequest;
import com.lycheetech.movingapi.invoice.dto.InvoiceResponse;
import com.lycheetech.movingapi.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final InvoiceStatusRepository invoiceStatusRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    public List<InvoiceResponse> findByBooking(UUID bookingId) {
        return invoiceRepository.findByBookingId(bookingId).stream().map(this::toResponse).toList();
    }

    public InvoiceResponse findById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    public InvoiceResponse create(InvoiceRequest request) {
        var booking = bookingRepository.findById(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));
        var status = invoiceStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice status not found: " + request.getStatusId()));
        var payment = request.getPaymentId() != null
                ? paymentRepository.findById(request.getPaymentId()).orElse(null)
                : null;

        Invoice invoice = Invoice.builder()
                .booking(booking)
                .payment(payment)
                .status(status)
                .amountDue(request.getAmountDue())
                .issueDate(request.getIssueDate())
                .dueDate(request.getDueDate())
                .build();
        return toResponse(invoiceRepository.save(invoice));
    }

    public InvoiceResponse update(UUID id, InvoiceRequest request) {
        Invoice invoice = getOrThrow(id);
        var status = invoiceStatusRepository.findById(request.getStatusId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice status not found: " + request.getStatusId()));
        var payment = request.getPaymentId() != null
                ? paymentRepository.findById(request.getPaymentId()).orElse(null)
                : null;
        invoice.setStatus(status);
        invoice.setPayment(payment);
        invoice.setAmountDue(request.getAmountDue());
        invoice.setIssueDate(request.getIssueDate());
        invoice.setDueDate(request.getDueDate());
        return toResponse(invoiceRepository.save(invoice));
    }

    public void delete(UUID id) {
        invoiceRepository.delete(getOrThrow(id));
    }

    private Invoice getOrThrow(UUID id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found: " + id));
    }

    private InvoiceResponse toResponse(Invoice i) {
        return InvoiceResponse.builder()
                .id(i.getId())
                .bookingId(i.getBooking().getId())
                .paymentId(i.getPayment() != null ? i.getPayment().getId() : null)
                .status(i.getStatus().getName())
                .amountDue(i.getAmountDue())
                .issueDate(i.getIssueDate())
                .dueDate(i.getDueDate())
                .createdAt(i.getCreatedAt())
                .build();
    }
}

