package com.payment.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(unique = true)
    private String reference;

    private String provider;

    private BigDecimal amount;

    private String status;

    private String paymentUrl;

    private String transactionId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
