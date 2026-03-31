package com.payment.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotBlank;

@Validated
@ConfigurationProperties(prefix = "wavepay")
public record WavePayProperties(

        @NotBlank String apiUrl,
        @NotBlank String apiKey,
        @NotBlank String merchantId

) {
}
