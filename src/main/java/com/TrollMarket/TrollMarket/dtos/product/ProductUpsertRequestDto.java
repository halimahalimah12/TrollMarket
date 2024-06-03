package com.TrollMarket.TrollMarket.dtos.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductUpsertRequestDto {
    private final Integer id;
//    private final Integer sellerId;
    @NotNull(message = "Please, Select Category!")
    private final Integer categoryId;
    @NotBlank (message = "Please, Entry Name Product!")
    @NotNull
    private final String name;
    private final String description;
    @NotNull(message = "Please, Entry Price!")
    private final String price;
    private final Boolean discountinue;

}
