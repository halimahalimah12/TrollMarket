package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.shop.ShopRowDto;
import com.TrollMarket.TrollMarket.dtos.shop.ShopSearchDto;
import com.TrollMarket.TrollMarket.models.Product;
import com.TrollMarket.TrollMarket.repositories.CartRepository;
import com.TrollMarket.TrollMarket.repositories.OrderRepository;
import com.TrollMarket.TrollMarket.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    private final CartRepository cartRepository;
    private  final ProductRepository productRepository;



    public ShopService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public Page<ShopRowDto> getAllProdcut(ShopSearchDto dto) {
        String nameProduct = dto.getNameProduct() == null || dto.getNameProduct().isBlank() ? null : dto.getNameProduct();
        Integer categoryId = (dto.getCategoryId() == null || dto.getCategoryId() == 0) ? null : dto.getCategoryId();
        String description = dto.getDescription() == null || dto.getDescription().isBlank() ? null : dto.getDescription();

        int pageNumber = dto.getPageNumber() == null ? 0 : dto.getPageNumber();
        int pageSize = dto.getPageSize() == null ? 10 : dto.getPageSize();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> productPage = productRepository.findAllForShop(pageable, nameProduct,categoryId,description);
        List<ShopRowDto> dtos = productPage.getContent()
                .stream().map(product -> ShopRowDto.builder()
                        .id(product.getId())
                        .product(product.getName())
                        .category(product.getCategory().getName())
                        .price(product.getPrice())
                        .build())
                .toList();
        return new PageImpl<>(dtos,pageable,productPage.getTotalElements());

    }

}
