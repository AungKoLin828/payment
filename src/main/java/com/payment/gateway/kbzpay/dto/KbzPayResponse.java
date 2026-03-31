package com.payment.gateway.kbzpay.dto;
import lombok.Data;

@Data
public class KbzPayResponse {

    private String paymentUrl;
    private String transactionId;
    private String status;

}
