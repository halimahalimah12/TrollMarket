package com.TrollMarket.TrollMarket.dtos;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class UploadFileToDbDto {
    private final MultipartFile file;
}
