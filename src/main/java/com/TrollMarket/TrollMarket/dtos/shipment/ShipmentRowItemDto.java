package com.TrollMarket.TrollMarket.dtos.shipment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShipmentRowItemDto {
    private final Integer id;
    private final String name;
    private final Double price;
    private final Integer totalOrder;
    private final String isService;
}
