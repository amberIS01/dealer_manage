package com.dealersautocenter.repository;

import com.dealersautocenter.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByDealerId(Long dealerId);
    List<Payment> findByStatus(Payment.PaymentStatus status);
}