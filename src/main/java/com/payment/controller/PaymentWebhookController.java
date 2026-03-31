package com.payment.controller;

import com.payment.dto.PaymentCallbackDto;
import com.payment.service.PaymentCallbackService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/webhook")
public class PaymentWebhookController {

    private final PaymentCallbackService callbackService;

    public PaymentWebhookController(PaymentCallbackService callbackService){
        this.callbackService = callbackService;
    }

    @PostMapping("/kbzpay")
    public ResponseEntity<String> kbzCallback(@RequestBody PaymentCallbackDto callback) {
        callbackService.handleCallback(callback);
        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/wavepay")
    public ResponseEntity<String> waveCallback(@RequestBody PaymentCallbackDto callback) {
        callbackService.handleCallback(callback);
        return ResponseEntity.ok("SUCCESS");
    }
}
