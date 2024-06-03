package com.TrollMarket.TrollMarket.dtos.utility;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectBuyerDto {
    private String text;
    private Integer value;
}
