package com.TrollMarket.TrollMarket.dtos.category;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryUpsertResponseDto {
    private Integer id;
    private String name;
    private String description;

}
