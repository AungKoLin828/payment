package com.payment.controller;

import com.payment.dto.PaymentCallbackDto;
import com.payment.service.PaymentCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments/webhook")
@RequiredArgsConstructor
public class PaymentWebhookController {

    private final PaymentCallbackService service;

    @PostMapping("/kbzpay")
    public ResponseEntity<String> kbzCallback(
            @RequestBody PaymentCallbackDto callback
    ) {

        service.handleCallback(callback);

        return ResponseEntity.ok("SUCCESS");
    }

    @PostMapping("/wavepay")
    public ResponseEntity<String> waveCallback(
            @RequestBody PaymentCallbackDto callback
    ) {

        service.handleCallback(callback);

        return ResponseEntity.ok("SUCCESS");
    }
}
