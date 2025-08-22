package com.dealersautocenter.service;

import com.dealersautocenter.dto.DealerDTO;
import com.dealersautocenter.entity.Dealer;
import com.dealersautocenter.exception.ResourceNotFoundException;
import com.dealersautocenter.exception.DuplicateResourceException;
import com.dealersautocenter.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DealerService {
    private final DealerRepository dealerRepository;
    
    public DealerDTO createDealer(DealerDTO dealerDTO) {
        if (dealerRepository.existsByEmail(dealerDTO.getEmail())) {
            throw new DuplicateResourceException("Dealer with email " + dealerDTO.getEmail() + " already exists");
        }
        
        Dealer dealer = new Dealer();
        dealer.setName(dealerDTO.getName());
        dealer.setEmail(dealerDTO.getEmail());
        dealer.setSubscriptionType(dealerDTO.getSubscriptionType());
        
        Dealer savedDealer = dealerRepository.save(dealer);
        return convertToDTO(savedDealer);
    }
    
    public DealerDTO updateDealer(Long id, DealerDTO dealerDTO) {
        Dealer dealer = dealerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dealer not found with id: " + id));
        
        if (!dealer.getEmail().equals(dealerDTO.getEmail()) && 
            dealerRepository.existsByEmail(dealerDTO.getEmail())) {
            throw new DuplicateResourceException("Dealer with email " + dealerDTO.getEmail() + " already exists");
        }
        
        dealer.setName(dealerDTO.getName());
        dealer.setEmail(dealerDTO.getEmail());
        dealer.setSubscriptionType(dealerDTO.getSubscriptionType());
        
        Dealer updatedDealer = dealerRepository.save(dealer);
        return convertToDTO(updatedDealer);
    }
    
    @Transactional(readOnly = true)
    public DealerDTO getDealerById(Long id) {
        Dealer dealer = dealerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Dealer not found with id: " + id));
        return convertToDTO(dealer);
    }
    
    @Transactional(readOnly = true)
    public List<DealerDTO> getAllDealers() {
        return dealerRepository.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }
    
    public void deleteDealer(Long id) {
        if (!dealerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dealer not found with id: " + id);
        }
        dealerRepository.deleteById(id);
    }
    
    private DealerDTO convertToDTO(Dealer dealer) {
        DealerDTO dto = new DealerDTO();
        dto.setId(dealer.getId());
        dto.setName(dealer.getName());
        dto.setEmail(dealer.getEmail());
        dto.setSubscriptionType(dealer.getSubscriptionType());
        return dto;
    }
}