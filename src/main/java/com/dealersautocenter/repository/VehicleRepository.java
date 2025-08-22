package com.dealersautocenter.repository;

import com.dealersautocenter.entity.Vehicle;
import com.dealersautocenter.entity.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByDealerId(Long dealerId);
    
    @Query("SELECT v FROM Vehicle v JOIN v.dealer d WHERE d.subscriptionType = 'PREMIUM'")
    List<Vehicle> findAllVehiclesOfPremiumDealers();
    
    List<Vehicle> findByStatus(Vehicle.VehicleStatus status);
}