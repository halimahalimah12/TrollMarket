package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Integer> {

    @Query("""
            SELECT COUNT(*)
            FROM Product p
            WHERE p.category.id = :id
            """)
    Integer countProductInCategory (@Param("id") Integer id);

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.seller.id = :sellerId
            """)
    List<Product> findAllProductSeller(@Param("sellerId") Integer sellerId);

    @Query("""
            SELECT p
            FROM Product p
            WHERE (:nameProduct IS NULL OR p.name LIKE %:nameProduct%)
            AND (:categoryId IS NULL OR p.category.id = :categoryId)
            AND (:description IS NULL OR p.description LIKE %:description%
                OR p.category.description LIKE %:description%)
            AND p.discontinue = false
            """)
    Page<Product> findAllForShop(Pageable pageable,
                                 @Param("nameProduct") String nameProduct,
                                 @Param("categoryId") Integer categoryId,
                                 @Param("description") String description);

    @Query("""
            SELECT p
            FROM Product p
            WHERE p.name LIKE %:name%
            """)
    Product findProductByName(@Param("name") String name);
}
