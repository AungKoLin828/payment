package com.payment.dto;
public record PaymentCallbackDto(String reference,
     String transactionId,
     String status,
     String provider) {
}
