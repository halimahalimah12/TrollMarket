package com.TrollMarket.TrollMarket.dtos.product;

import com.TrollMarket.TrollMarket.models.Category;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRowDto {
    private final Integer id;
    private final String name;
    private final Category category;
    private final String discontinue;
    private final Integer totalProductInOrder;
}
