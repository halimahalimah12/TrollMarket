package com.TrollMarket.TrollMarket.services;


import com.TrollMarket.TrollMarket.dtos.category.CategoryUpsertRequestDto;
import com.TrollMarket.TrollMarket.dtos.category.CategoryUpsertResponseDto;
import com.TrollMarket.TrollMarket.dtos.product.ProductRowDto;
import com.TrollMarket.TrollMarket.dtos.product.ProductUpsertRequestDto;
import com.TrollMarket.TrollMarket.dtos.product.ProductUpsertResponseDto;
import com.TrollMarket.TrollMarket.dtos.utility.SelectListCategoryDto;
import com.TrollMarket.TrollMarket.models.Category;
import com.TrollMarket.TrollMarket.models.Product;
import com.TrollMarket.TrollMarket.models.Seller;
import com.TrollMarket.TrollMarket.repositories.CategoryRepository;
import com.TrollMarket.TrollMarket.repositories.OrderRepository;
import com.TrollMarket.TrollMarket.repositories.ProductRepository;

import com.TrollMarket.TrollMarket.repositories.SellerRepository;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SellerRepository sellerRepository;
    private final OrderRepository orderRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SellerRepository sellerRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.sellerRepository = sellerRepository;
        this.orderRepository = orderRepository;
    }

    public List<ProductRowDto> getAll(Seller seller) {

        List<Product> products = productRepository.findAllProductSeller(seller.getId());

        List<ProductRowDto> productRowDtoList = products.stream().map(
                        product -> ProductRowDto.builder()
                                .id(product.getId())
                                .name(product.getName())
                                .category(product.getCategory())
                                .discontinue(product.getDiscontinue() == false ? "No" : "Yes" )
                                .totalProductInOrder(countProductInOrder(product.getId()))
                                .build())
                .toList();

        return productRowDtoList;
    }

    public List<SelectListCategoryDto> getCategoryDropdown() {
        return categoryRepository.findAll().stream()
                .map(category -> SelectListCategoryDto.builder()
                        .value(category.getId())
                        .text(category.getName())
                        .build())
                .toList();
    }

    public ProductUpsertResponseDto save(ProductUpsertRequestDto dto,Integer sellerId) {
        Seller seller =findSeller(sellerId);
        Category category = findCategory(dto.getCategoryId());
        var price = dto.getPrice().replace(".","");
        Double priceFix = Double.parseDouble(price);

        var product = Product.builder()
                .id(dto.getId())
                .seller(seller)
                .category(category)
                .name(dto.getName())
                .price(priceFix)
                .description(dto.getDescription())
                .discontinue(dto.getDiscountinue() == null ? false : dto.getDiscountinue())
                .build();

        product = productRepository.save(product);

        return convertResponseDto(product);
    }

    private Seller findSeller(Integer sellerId) {
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new  IllegalArgumentException("Seller not found"));
    }

    private Category findCategory(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
    }

    public ProductUpsertResponseDto convertResponseDto(Product product) {
        return ProductUpsertResponseDto.builder()
                .id(product.getId())
                .sellerId(product.getSeller().getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .discountinue(product.getDiscontinue())
                .build();

    }

    public ProductUpsertRequestDto getProductById(Integer id){
        Product product = findProduct(id);

        return convertRequestDto(product);
    }
    private Product findProduct(Integer id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }

    public ProductUpsertRequestDto convertRequestDto(Product product) {
        return ProductUpsertRequestDto.builder()
                .id(product.getId())
                .categoryId(product.getCategory().getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice().toString())
                .discountinue(product.getDiscontinue())
                .build();

    }
    private Integer countProductInOrder(Integer id){
        return  orderRepository.countProduct(id);

    }



}
