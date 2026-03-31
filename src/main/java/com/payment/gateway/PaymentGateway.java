package com.payment.gateway;


import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;

public interface PaymentGateway {

    String getProvider();

    PaymentResponseDto createPayment(PaymentRequestDto request);

}