package com.payment.dto;

import java.math.BigDecimal;

public record PaymentResponseDto(

        String reference,
        String paymentUrl,
        String provider,
        BigDecimal amount,
        String status

) {
}