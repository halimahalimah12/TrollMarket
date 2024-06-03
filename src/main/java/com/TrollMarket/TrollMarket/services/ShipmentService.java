package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.category.CategoryRowItemDto;
import com.TrollMarket.TrollMarket.dtos.shipment.ShipmentRowItemDto;
import com.TrollMarket.TrollMarket.models.Category;
import com.TrollMarket.TrollMarket.models.Shipment;
import com.TrollMarket.TrollMarket.repositories.OrderRepository;
import com.TrollMarket.TrollMarket.repositories.ShipmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipmentService {
    private final ShipmentRepository shipmentRepository;
    public  final OrderRepository orderRepository;

    public ShipmentService(ShipmentRepository shipmentRepository, OrderRepository orderRepository) {
        this.shipmentRepository = shipmentRepository;
        this.orderRepository = orderRepository;
    }

    public Page<ShipmentRowItemDto> getAll() {
        int pageNumber =  0 ;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Shipment> shipmentPage = shipmentRepository.findAll(pageable);
        List<ShipmentRowItemDto> dtos = shipmentPage.getContent()
                .stream().map(shipment -> ShipmentRowItemDto.builder()
                        .id(shipment.getId())
                        .name(shipment.getName())
                        .price(shipment.getPrice())
                        .totalOrder(countShipmenFromOrder(shipment.getId()))
                        .isService(shipment.getIsService()== true ? "Yes" : "No")
                        .build())
                .toList();
        return new PageImpl<>(dtos, pageable, shipmentPage.getTotalElements());
    }

    private Integer countShipmenFromOrder(Integer id){
        return orderRepository.countShipment(id);
    }



}
