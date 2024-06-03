package com.TrollMarket.TrollMarket.services;

import com.TrollMarket.TrollMarket.dtos.category.*;
import com.TrollMarket.TrollMarket.models.Category;
import com.TrollMarket.TrollMarket.repositories.CategoryRepository;
import com.TrollMarket.TrollMarket.repositories.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Page<CategoryRowItemDto> getAll(CategorySearchDto dto) {
        String name = dto.getName() == null || dto.getName().isBlank() ? null : dto.getName();

        int pageNumber = dto.getPageNumber() == null ? 0 : dto.getPageNumber();
        int pageSize = dto.getPageSize() == null ? 10 : dto.getPageSize();
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Category> CategoryPage = categoryRepository.findAll(pageable, name);
        List<CategoryRowItemDto> dtos = CategoryPage.getContent()
                .stream().map(category -> CategoryRowItemDto.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .totalProduct(getTotalProduct(category.getId()))
                        .build())
                .toList();
        return new PageImpl<>(dtos, pageable, CategoryPage.getTotalElements());
    }

    public CategoryUpsertResponseDto save(CategoryUpsertRequestDto dto) {
        var category = Category.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .build();

        category = categoryRepository.save(category);

        return convertResponseDto(category);
    }

    private CategoryUpsertResponseDto convertResponseDto(Category category) {
        return CategoryUpsertResponseDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public CategoryUpsertRequestDto getById(Integer id) {
        Category category = cekFindByID(id);
        return convertCategoryUpsertRequestDto(category);
    }
    private Category cekFindByID(Integer id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Id Category tidak ditemukan"));
    }

    private CategoryUpsertRequestDto convertCategoryUpsertRequestDto(Category category) {
        return CategoryUpsertRequestDto.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public void deleteById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public Integer getTotalProduct(Integer id) {
        return productRepository.countProductInCategory(id);
    }

    public CategoryDeleteItemDto getItemByIdForDelete(Integer id) {
        Category category = cekFindByID(id);
        return CategoryDeleteItemDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }



}
