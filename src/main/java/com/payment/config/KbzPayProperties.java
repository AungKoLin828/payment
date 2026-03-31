package com.payment.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "kbzpay")
public record KbzPayProperties(

        @NotBlank
        String apiUrl,

        @NotBlank
        String apiKey,

        @NotBlank
        String merchantId
) {
}