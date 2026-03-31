package com.payment.dto;

import java.math.BigDecimal;

public record PaymentCallbackDto(String reference,
    String transactionId,
    String status,
    BigDecimal amount) {
    
}
