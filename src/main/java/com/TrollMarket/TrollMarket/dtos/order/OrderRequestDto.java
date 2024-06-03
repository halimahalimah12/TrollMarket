package com.TrollMarket.TrollMarket.dtos.order;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderRequestDto {
    private final Integer buyerId;
}
