package com.payment.service;

import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;

public interface PaymentService {
     PaymentResponseDto createPayment(PaymentRequestDto request);
}
