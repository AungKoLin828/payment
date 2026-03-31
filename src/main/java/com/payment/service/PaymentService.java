package com.payment.service;

import com.payment.domain.PaymentStatus;
import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;

public interface PaymentService {
     PaymentResponseDto createPayment(PaymentRequestDto request);
     // Update payment status (called by webhook)
    void updatePaymentStatus(String reference, PaymentStatus status, String transactionId);
}
