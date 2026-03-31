package com.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PaymentRequestDto(

        @NotNull
        Long userId,

        @NotNull
        @Positive
        BigDecimal amount,

        @NotBlank
        String provider,

        String reference

) {
}