package com.payment.service;

import com.payment.dto.PaymentCallbackDto;

public interface PaymentCallbackService {
    void handleCallback(PaymentCallbackDto callback);
}
