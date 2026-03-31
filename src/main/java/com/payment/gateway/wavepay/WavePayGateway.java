package com.payment.gateway.wavepay;

import com.payment.config.WavePayProperties;
import com.payment.domain.PaymentStatus;
import com.payment.dto.PaymentRequestDto;
import com.payment.dto.PaymentResponseDto;
import com.payment.gateway.PaymentGateway;
import com.payment.gateway.wavepay.dto.WavePayRequest;
import com.payment.gateway.wavepay.dto.WavePayResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class WavePayGateway implements PaymentGateway {

    private final WebClient webClient;
    private final WavePayProperties properties;

    @Override
    public String getProvider() {
        return "WAVEPAY";
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto request) {

        log.info("Calling WavePay API for user {}", request.userId());

        WavePayRequest waveRequest = mapToRequest(request);

        WavePayResponse response = webClient
                .post()
                .uri(properties.apiUrl() + "/payment/create")
                .header("Authorization", properties.apiKey())
                .bodyValue(waveRequest)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse ->
                                clientResponse.bodyToMono(String.class)
                                        .flatMap(error -> {
                                            log.error("WavePay API Error {}", error);
                                            return Mono.error(
                                                    new RuntimeException("WavePay API Error")
                                            );
                                        })
                )
                .bodyToMono(WavePayResponse.class)
                .timeout(Duration.ofSeconds(10))
                .block();

        log.info("WavePay response received");

        return mapResponse(request, response);
    }

    private WavePayRequest mapToRequest(PaymentRequestDto request) {
        return WavePayRequest.builder()
                .merchantId(properties.merchantId())
                .reference(request.reference())
                .userId(request.userId())
                .amount(request.amount())
                .build();
    }

    private PaymentResponseDto mapResponse(PaymentRequestDto request, WavePayResponse response) {
        PaymentStatus status = mapStatus(response.getStatus());
        return new PaymentResponseDto(
                request.reference(),
                response.getPaymentUrl(),
                "WAVEPAY",
                request.amount(),
                status.name()
        );
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