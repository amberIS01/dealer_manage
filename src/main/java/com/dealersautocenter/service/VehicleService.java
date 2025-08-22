package com.dealersautocenter.service;

import com.dealersautocenter.dto.VehicleDTO;
import com.dealersautocenter.entity.Dealer;
import com.dealersautocenter.entity.Vehicle;
import com.dealersautocenter.exception.ResourceNotFoundException;
import com.dealersautocenter.repository.DealerRepository;
import com.dealersautocenter.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;
    
    public VehicleDTO createVehicle(VehicleDTO vehicleDTO) {
        Dealer dealer = dealerRepository.findById(vehicleDTO.getDealerId())
            .orElseThrow(() -> new ResourceNotFoundException("Dealer not found with id: " + vehicleDTO.getDealerId()));
        
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setStatus(vehicleDTO.getStatus());
        vehicle.setDealer(dealer);
        
        Vehicle savedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(savedVehicle);
    }
    
    public VehicleDTO updateVehicle(Long id, VehicleDTO vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        
        if (vehicle.getDealerId() == null || !vehicle.getDealerId().equals(vehicleDTO.getDealerId())) {
            Dealer dealer = dealerRepository.findById(vehicleDTO.getDealerId())
                .orElseThrow(() -> new ResourceNotFoundException("Dealer not found with id: " + vehicleDTO.getDealerId()));
            vehicle.setDealer(dealer);
        }
        
        vehicle.setModel(vehicleDTO.getModel());
        vehicle.setPrice(vehicleDTO.getPrice());
        vehicle.setStatus(vehicleDTO.getStatus());
        
        Vehicle updatedVehicle = vehicleRepository.save(vehicle);
        return convertToDTO(updatedVehicle);
    }
    
    @Transactional(readOnly = true)
    public VehicleDTO getVehicleById(Long id) {
        Vehicle vehicle = vehicleRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id: " + id));
        return convertToDTO(vehicle);
    }
    
    @Transactional(readOnly = true)
    public List<VehicleDTO> getAllVehicles() {
        return vehicleRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<VehicleDTO> getVehiclesByDealerId(Long dealerId) {
        return vehicleRepository.findByDealerId(dealerId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<VehicleDTO> getPremiumDealerVehicles() {
        return vehicleRepository.findAllVehiclesOfPremiumDealers().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found with id: " + id);
        }
        vehicleRepository.deleteById(id);
    }
    
    private VehicleDTO convertToDTO(Vehicle vehicle) {
        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setDealerId(vehicle.getDealerId() != null ? vehicle.getDealerId() : 
                        (vehicle.getDealer() != null ? vehicle.getDealer().getId() : null));
        dto.setModel(vehicle.getModel());
        dto.setPrice(vehicle.getPrice());
        dto.setStatus(vehicle.getStatus());
        return dto;
    }
}