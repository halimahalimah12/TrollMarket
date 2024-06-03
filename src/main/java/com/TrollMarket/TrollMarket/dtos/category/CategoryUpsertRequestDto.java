package com.TrollMarket.TrollMarket.dtos.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryUpsertRequestDto {
    private Integer id;
    @NotBlank(message = "Please, Entry Category Name!")
    @NotNull
    private String name;
    private String description;

}
