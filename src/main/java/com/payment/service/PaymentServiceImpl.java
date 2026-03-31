package com.payment.service;

import com.payment.domain.Payment;
import com.payment.domain.PaymentStatus;
import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.gateway.factory.PaymentGatewayFactory;
import com.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository repository;
    private final PaymentGatewayFactory factory;

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        String reference = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .userId(request.userId())
                .amount(request.amount())
                .provider(request.provider())
                .reference(reference)
                .status(PaymentStatus.PENDING.name())
                .createdAt(LocalDateTime.now())
                .build();

        repository.save(payment);

        var response = factory
                .getGateway(request.provider())
                .createPayment(request);

        payment.setPaymentUrl(response.paymentUrl());

        repository.save(payment);

        return new PaymentResponseDto(
                reference,
                response.paymentUrl(),
                request.provider(),
                request.amount(),
                payment.getStatus()
        );
    }
}