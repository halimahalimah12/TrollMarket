package com.TrollMarket.TrollMarket.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpsertResponseDto {
    private final Integer id;
    private final Integer sellerId;
    private final Integer categoryId;
    private final String name;
    private final String description;
    private final Double price;
    private final Boolean discountinue;

}
