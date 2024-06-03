package com.TrollMarket.TrollMarket.dtos.order;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class OrderRowDto {
    private  final LocalDate date;
    private  final String seller;
    private final String buyer;
    private final String product;
    private final Integer quantity;
    private final String shipment;
    private final Double totalPrice;
}
