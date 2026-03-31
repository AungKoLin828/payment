package com.payment.gateway.kbzpay;

import com.payment.config.KbzPayProperties;
import com.payment.domain.PaymentStatus;
import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.gateway.PaymentGateway;
import com.payment.gateway.kbzpay.dto.KbzPayRequest;
import com.payment.gateway.kbzpay.dto.KbzPayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class KBZPayGateway implements PaymentGateway {

    private final WebClient webClient;
    private final KbzPayProperties properties;

    @Override
    public String getProvider() {
        return "KBZPAY";
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        log.info("Calling KBZPay API for user {}", request.userId());

        KbzPayRequest kbzRequest = mapToRequest(request);

        KbzPayResponse response = webClient
                .post()
                .uri(properties.apiUrl() + "/payment/create")
                .header("Authorization", properties.apiKey())
                .bodyValue(kbzRequest)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .flatMap(error -> {
                                            log.error("KBZPay error {}", error);
                                            return Mono.error(
                                                    new RuntimeException("KBZPay API Error")
                                            );
                                        })
                )
                .bodyToMono(KbzPayResponse.class)
                .timeout(java.time.Duration.ofSeconds(10))
                .block();

        log.info("KBZPay response received");

       return mapResponse(request, response);
    }

    private PaymentResponseDto mapResponse(PaymentRequestDto request, KbzPayResponse response) {
        PaymentStatus status = mapStatus(response.getStatus());
        return new PaymentResponseDto(
                request.reference(),
                response.getPaymentUrl(),
                "KBZPAY",
                request.amount(),
                status.name()
        );
    }

    private KbzPayRequest mapToRequest(PaymentRequestDto request) {

        return KbzPayRequest.builder()
                .merchantId(properties.merchantId())
                .amount(request.amount())
                .reference(request.reference())
                .userId(request.userId())
                .build();
    }

    private PaymentStatus mapStatus(String status) {

        if (status == null) {
            return PaymentStatus.PENDING;
        }

        return switch (status.toUpperCase()) {
            case "SUCCESS", "COMPLETED", "PAID" -> PaymentStatus.SUCCESS;
            case "FAILED", "ERROR" -> PaymentStatus.FAILED;
            case "EXPIRED", "TIMEOUT" -> PaymentStatus.EXPIRED;
            default -> PaymentStatus.PENDING;
        };
    }
}
