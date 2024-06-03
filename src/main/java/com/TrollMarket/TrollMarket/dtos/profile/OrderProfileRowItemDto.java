package com.TrollMarket.TrollMarket.dtos.profile;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OrderProfileRowItemDto {
    private final LocalDate date;
    private final String nameProduct;
    private final Integer quantity;
    private final String nameShipment;
    private final Double totalPrice;
}
