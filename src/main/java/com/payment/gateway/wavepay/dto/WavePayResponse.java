package com.payment.gateway.wavepay.dto;

import lombok.Data;

@Data
public class WavePayResponse {

    private String paymentUrl;
    private String transactionId;
    private String status;

}
