package com.dealersautocenter.dto;

import com.dealersautocenter.entity.Vehicle;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDTO {
    private Long id;
    
    @NotNull(message = "Dealer ID is required")
    private Long dealerId;
    
    @NotBlank(message = "Model is required")
    private String model;
    
    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal price;
    
    @NotNull(message = "Status is required")
    private Vehicle.VehicleStatus status;
}