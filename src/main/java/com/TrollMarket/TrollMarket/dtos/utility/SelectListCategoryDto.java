package com.TrollMarket.TrollMarket.dtos.utility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectListCategoryDto {
    private String text;
    private Integer value;

}
