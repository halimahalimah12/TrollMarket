package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Cart;
import com.TrollMarket.TrollMarket.models.CartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, CartId> {

    @Query("""
            SELECT c
            FROM Cart c
            WHERE c.buyer.id = :buyerId
            """)
    List<Cart> findAllByBuyer(@Param("buyerId") Integer buyerId);

    @Query("""
            SELECT COUNT(*)
            FROM Cart c
            WHERE c.buyer.id = :buyerId
            """)
    Integer countByBuyer(@Param("buyerId") Integer buyerId);
}
