package com.payment.gateway.factory;

import com.payment.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentGatewayFactory {

    private final List<PaymentGateway> gateways;

    public PaymentGateway getGateway(String provider) {

        return gateways.stream()
                .filter(g -> g.getProvider().equalsIgnoreCase(provider))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Unsupported provider: " + provider)
                );
    }
}