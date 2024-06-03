package com.TrollMarket.TrollMarket.dtos.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryRowItemDto {
    private final Integer id;
    private final String name;
    private final String description;
    private final Integer totalProduct;
}
