package com.payment.service;

import com.payment.domain.Payment;
import com.payment.domain.PaymentStatus;
import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.gateway.factory.PaymentGatewayFactory;
import com.payment.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PaymentGatewayFactory factory;

    public PaymentServiceImpl(PaymentRepository repository,PaymentGatewayFactory factory){
        this.repository = repository;
        this.factory = factory;
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        // Generate unique reference
        String reference = UUID.randomUUID().toString();

        // Save payment entity with PENDING status
        Payment payment = Payment.builder()
                .userId(request.userId())
                .amount(request.amount())
                .provider(request.provider())
                .reference(reference)
                .status(PaymentStatus.PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(payment);

        // Call payment gateway and pass the reference
        PaymentRequestDto gatewayRequest = new PaymentRequestDto(
                request.userId(),
                request.amount(),
                request.provider(),
                reference
        );

        PaymentResponseDto gatewayResponse = factory
                .getGateway(request.provider())
                .createPayment(gatewayRequest);

        // Since record is immutable, create a **new PaymentResponseDto** for client
        PaymentResponseDto response = new PaymentResponseDto(
                reference,
                gatewayResponse.paymentUrl(),
                request.provider(),
                request.amount(),
                payment.getStatus() // always PENDING at this stage
        );

        // Update DB entity with paymentUrl (not DTO)
        payment.setPaymentUrl(gatewayResponse.paymentUrl());
        repository.save(payment);

        return response;
    }

    @Override
    public void updatePaymentStatus(String reference, PaymentStatus status, String transactionId) {

        repository.findByReference(reference).ifPresent(payment -> {
            payment.setStatus(status.name());
            payment.setTransactionId(transactionId);
            payment.setUpdatedAt(LocalDateTime.now());
            repository.save(payment);
            log.info("Payment {} updated to status {}", reference, status);
        });
    }
}