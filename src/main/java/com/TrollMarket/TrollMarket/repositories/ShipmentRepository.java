package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShipmentRepository extends JpaRepository<Shipment,Integer> {
    @Query("""
            SELECT s
            FROM Shipment s
            """)
    Page<Shipment> findAll(Pageable pageable);

    @Query("""
            SELECT s
            FROM Shipment s
            WHERE s.name LIKE %:name%
            """)
    Shipment shipmentFindByName (@Param("name") String name);

    @Query("""
            SELECT s.price
            FROM Shipment s
            WHERE s.name LIKE %:name%
            """)
    Double priceShipment (@Param("name") String name);

}
