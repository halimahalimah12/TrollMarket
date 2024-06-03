package com.TrollMarket.TrollMarket.dtos.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderSearchDto {
    private final Integer seller;
    private final Integer buyer;
    private Integer pageNumber;
    private Integer pageSize;
}
