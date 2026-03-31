package com.payment.service;


import com.payment.domain.Payment;
import com.payment.repository.PaymentRepository;
import com.payment.dto.PaymentCallbackDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentCallbackServiceImpl
        implements PaymentCallbackService {

    private final PaymentRepository repository;

    @Override
    public void handleCallback(PaymentCallbackDto callback) {

        Payment payment = repository
                .findByReference(callback.reference())
                .orElseThrow(() ->
                        new RuntimeException("Payment not found")
                );

        payment.setStatus(callback.status());
        payment.setTransactionId(callback.transactionId());
        payment.setUpdatedAt(LocalDateTime.now());

        repository.save(payment);

        log.info("Payment updated {}", callback.reference());
    }
}
