package com.TrollMarket.TrollMarket.dtos.profile;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder

public class ProfileUpsertRequestDto {
    private final int id  ;
    private final String accountId;
    private final String name;
    private final String address;
    private final Double balance;
    private final MultipartFile photo;
    private final String role;
}
