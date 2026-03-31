package com.payment.gateway.kbzpay.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class KbzPayRequest {

    private String merchantId;
    private String reference;
    private Long userId;
    private BigDecimal amount;

}
