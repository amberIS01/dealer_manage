package com.dealersautocenter.controller;

import com.dealersautocenter.dto.DealerDTO;
import com.dealersautocenter.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/dealers")
@RequiredArgsConstructor
@Tag(name = "Dealer Management", description = "APIs for managing dealers")
public class DealerController {
    private final DealerService dealerService;
    
    @PostMapping
    @Operation(summary = "Create a new dealer")
    public ResponseEntity<DealerDTO> createDealer(@Valid @RequestBody DealerDTO dealerDTO) {
        DealerDTO createdDealer = dealerService.createDealer(dealerDTO);
        return new ResponseEntity<>(createdDealer, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get dealer by ID")
    public ResponseEntity<DealerDTO> getDealerById(@PathVariable Long id) {
        DealerDTO dealer = dealerService.getDealerById(id);
        return ResponseEntity.ok(dealer);
    }
    
    @GetMapping
    @Operation(summary = "Get all dealers")
    public ResponseEntity<List<DealerDTO>> getAllDealers() {
        List<DealerDTO> dealers = dealerService.getAllDealers();
        return ResponseEntity.ok(dealers);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing dealer")
    public ResponseEntity<DealerDTO> updateDealer(@PathVariable Long id, @Valid @RequestBody DealerDTO dealerDTO) {
        DealerDTO updatedDealer = dealerService.updateDealer(id, dealerDTO);
        return ResponseEntity.ok(updatedDealer);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a dealer")
    public ResponseEntity<Void> deleteDealer(@PathVariable Long id) {
        dealerService.deleteDealer(id);
        return ResponseEntity.noContent().build();
    }
}