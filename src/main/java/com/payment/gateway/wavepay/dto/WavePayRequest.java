package com.payment.gateway.wavepay.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class WavePayRequest {

    private String merchantId;
    private String reference;
    private Long userId;
    private BigDecimal amount;

}
