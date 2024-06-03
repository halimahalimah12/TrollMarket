package com.TrollMarket.TrollMarket.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SelectListRoleDto {
    private Integer id;
    private String roleName;
}
