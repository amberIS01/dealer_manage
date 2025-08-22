package com.dealersautocenter.controller;

import com.dealersautocenter.dto.PaymentRequest;
import com.dealersautocenter.dto.PaymentResponse;
import com.dealersautocenter.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Tag(name = "Payment Processing", description = "APIs for payment processing")
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {
    private final PaymentService paymentService;
    
    @PostMapping("/initiate")
    @Operation(summary = "Initiate a payment")
    public ResponseEntity<PaymentResponse> initiatePayment(@Valid @RequestBody PaymentRequest paymentRequest) {
        PaymentResponse response = paymentService.initiatePayment(paymentRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    public ResponseEntity<PaymentResponse> getPaymentById(@PathVariable Long id) {
        PaymentResponse payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }
    
    @GetMapping
    @Operation(summary = "Get all payments")
    public ResponseEntity<List<PaymentResponse>> getAllPayments() {
        List<PaymentResponse> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }
    
    @GetMapping("/dealer/{dealerId}")
    @Operation(summary = "Get payments by dealer ID")
    public ResponseEntity<List<PaymentResponse>> getPaymentsByDealerId(@PathVariable Long dealerId) {
        List<PaymentResponse> payments = paymentService.getPaymentsByDealerId(dealerId);
        return ResponseEntity.ok(payments);
    }
}