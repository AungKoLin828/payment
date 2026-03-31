package com.payment.controller;

import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(
            @Valid @RequestBody PaymentRequestDto request
    ) {
        return ResponseEntity.ok(
                service.createPayment(request)
        );
    }
}