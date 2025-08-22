package com.dealersautocenter.service;

import com.dealersautocenter.dto.PaymentRequest;
import com.dealersautocenter.dto.PaymentResponse;
import com.dealersautocenter.entity.Dealer;
import com.dealersautocenter.entity.Payment;
import com.dealersautocenter.exception.ResourceNotFoundException;
import com.dealersautocenter.repository.DealerRepository;
import com.dealersautocenter.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final DealerRepository dealerRepository;
    
    public PaymentResponse initiatePayment(PaymentRequest request) {
        Dealer dealer = dealerRepository.findById(request.getDealerId())
            .orElseThrow(() -> new ResourceNotFoundException("Dealer not found with id: " + request.getDealerId()));
        
        Payment payment = new Payment();
        payment.setDealer(dealer);
        payment.setAmount(request.getAmount());
        payment.setMethod(request.getMethod());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        
        Payment savedPayment = paymentRepository.save(payment);
        
        // Simulate payment processing with 5-second delay
        processPaymentAsync(savedPayment.getId());
        
        return convertToResponse(savedPayment, "Payment initiated successfully. Processing in progress...");
    }
    
    @Async
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW)
    public void processPaymentAsync(Long paymentId) {
        CompletableFuture.runAsync(() -> {
            try {
                // Wait for 5 seconds
                TimeUnit.SECONDS.sleep(5);
                
                // Update payment status to SUCCESS with proper error handling
                Payment payment = paymentRepository.findById(paymentId).orElse(null);
                if (payment != null && payment.getStatus() == Payment.PaymentStatus.PENDING) {
                    payment.setStatus(Payment.PaymentStatus.SUCCESS);
                    paymentRepository.save(payment);
                    log.info("Payment {} processed successfully", paymentId);
                } else if (payment == null) {
                    log.error("Payment {} not found for processing", paymentId);
                } else {
                    log.warn("Payment {} already processed with status: {}", paymentId, payment.getStatus());
                }
            } catch (InterruptedException e) {
                log.error("Error processing payment {}: {}", paymentId, e.getMessage());
                Thread.currentThread().interrupt();
                // Try to mark payment as failed
                try {
                    Payment payment = paymentRepository.findById(paymentId).orElse(null);
                    if (payment != null && payment.getStatus() == Payment.PaymentStatus.PENDING) {
                        payment.setStatus(Payment.PaymentStatus.FAILED);
                        paymentRepository.save(payment);
                    }
                } catch (Exception ex) {
                    log.error("Failed to update payment status to FAILED for payment {}: {}", paymentId, ex.getMessage());
                }
            } catch (Exception e) {
                log.error("Unexpected error processing payment {}: {}", paymentId, e.getMessage());
                // Try to mark payment as failed
                try {
                    Payment payment = paymentRepository.findById(paymentId).orElse(null);
                    if (payment != null && payment.getStatus() == Payment.PaymentStatus.PENDING) {
                        payment.setStatus(Payment.PaymentStatus.FAILED);
                        paymentRepository.save(payment);
                    }
                } catch (Exception ex) {
                    log.error("Failed to update payment status to FAILED for payment {}: {}", paymentId, ex.getMessage());
                }
            }
        });
    }
    
    @Transactional(readOnly = true)
    public PaymentResponse getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return convertToResponse(payment, null);
    }
    
    @Transactional(readOnly = true)
    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream()
            .map(payment -> convertToResponse(payment, null))
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PaymentResponse> getPaymentsByDealerId(Long dealerId) {
        return paymentRepository.findByDealerId(dealerId).stream()
            .map(payment -> convertToResponse(payment, null))
            .collect(Collectors.toList());
    }
    
    private PaymentResponse convertToResponse(Payment payment, String message) {
        PaymentResponse response = new PaymentResponse();
        response.setId(payment.getId());
        response.setDealerId(payment.getDealerId());
        response.setAmount(payment.getAmount());
        response.setMethod(payment.getMethod());
        response.setStatus(payment.getStatus());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        response.setMessage(message);
        return response;
    }
}