package com.payment.service;

import com.payment.domain.PaymentStatus;
import com.payment.repository.PaymentRepository;
import com.payment.dto.PaymentCallbackDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class PaymentCallbackServiceImpl implements PaymentCallbackService {

    private final PaymentRepository repository;

    public PaymentCallbackServiceImpl(PaymentRepository repository){
        this.repository = repository;
    }

    @Override
    public void handleCallback(PaymentCallbackDto callback) {

        repository.findByReference(callback.reference()).ifPresentOrElse(payment -> {

            PaymentStatus mappedStatus = mapStatus(callback.status());

            payment.setStatus(mappedStatus.name());
            payment.setTransactionId(callback.transactionId());
            payment.setUpdatedAt(LocalDateTime.now());

            repository.save(payment);

            log.info("Payment {} updated to {} via {}",
                    payment.getReference(),
                    mappedStatus,
                    callback.provider());

        }, () -> log.warn("Payment not found for reference {}", callback.reference()));
    }

    private PaymentStatus mapStatus(String status) {
        if (status == null) return PaymentStatus.PENDING;

        return switch (status.toUpperCase()) {
            case "SUCCESS", "PAID" -> PaymentStatus.SUCCESS;
            case "FAILED", "ERROR" -> PaymentStatus.FAILED;
            case "EXPIRED", "TIMEOUT" -> PaymentStatus.EXPIRED;
            default -> PaymentStatus.PENDING;
        };
    }
}
