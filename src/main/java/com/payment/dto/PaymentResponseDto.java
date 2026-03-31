package com.payment.dto;

import java.math.BigDecimal;

public record PaymentResponseDto(

      
        String paymentUrl,
        String provider,
        BigDecimal amount,
        String status,
          String reference

) {
}