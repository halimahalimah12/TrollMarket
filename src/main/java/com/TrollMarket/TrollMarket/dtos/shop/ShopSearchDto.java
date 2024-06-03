package com.TrollMarket.TrollMarket.dtos.shop;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShopSearchDto {
    private final String nameProduct;
    private final Integer categoryId;
    private final String description;
    private final Integer pageNumber;
    private final Integer pageSize;

}
