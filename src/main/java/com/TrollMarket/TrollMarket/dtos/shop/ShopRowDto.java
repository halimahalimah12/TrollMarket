package com.TrollMarket.TrollMarket.dtos.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopRowDto {
    private final Integer id;
    private final  String product;
    private final String category;
    private final Double price;
}
