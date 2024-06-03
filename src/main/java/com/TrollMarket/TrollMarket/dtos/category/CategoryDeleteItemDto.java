package com.TrollMarket.TrollMarket.dtos.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryDeleteItemDto {
    private  final Integer id;
    private final String name;

}
