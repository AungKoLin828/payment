package com.payment.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.payment.domain.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
     Optional<Payment> findByReference(String reference);
}
