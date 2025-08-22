package com.dealersautocenter.controller;

import com.dealersautocenter.dto.VehicleDTO;
import com.dealersautocenter.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle Management", description = "APIs for managing vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    
    @PostMapping
    @Operation(summary = "Create a new vehicle")
    public ResponseEntity<VehicleDTO> createVehicle(@Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO createdVehicle = vehicleService.createVehicle(vehicleDTO);
        return new ResponseEntity<>(createdVehicle, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by ID")
    public ResponseEntity<VehicleDTO> getVehicleById(@PathVariable Long id) {
        VehicleDTO vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }
    
    @GetMapping
    @Operation(summary = "Get all vehicles")
    public ResponseEntity<List<VehicleDTO>> getAllVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/dealer/{dealerId}")
    @Operation(summary = "Get vehicles by dealer ID")
    public ResponseEntity<List<VehicleDTO>> getVehiclesByDealerId(@PathVariable Long dealerId) {
        List<VehicleDTO> vehicles = vehicleService.getVehiclesByDealerId(dealerId);
        return ResponseEntity.ok(vehicles);
    }
    
    @GetMapping("/premium")
    @Operation(summary = "Get all vehicles belonging to PREMIUM dealers")
    public ResponseEntity<List<VehicleDTO>> getPremiumDealerVehicles() {
        List<VehicleDTO> vehicles = vehicleService.getPremiumDealerVehicles();
        return ResponseEntity.ok(vehicles);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing vehicle")
    public ResponseEntity<VehicleDTO> updateVehicle(@PathVariable Long id, @Valid @RequestBody VehicleDTO vehicleDTO) {
        VehicleDTO updatedVehicle = vehicleService.updateVehicle(id, vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vehicle")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }
}