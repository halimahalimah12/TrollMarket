package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShipmentRepository extends JpaRepository<Shipment,Integer> {
    @Query("""
            SELECT s
            FROM Shipment s
            """)
    Page<Shipment> findAll(Pageable pageable);
}
