package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    @Query("""
            SELECT COUNT(*)
            FROM Order o
            WHERE o.id = :orderId
            """)
    Integer countShipment(@Param("orderId") Integer orderId);

    @Query("""
            SELECT o
            FROM Order o
            WHERE o.buyer.id = :buyerId
            """)
    List<Order> orderListByBuyer(@Param("buyerId") Integer buyerId);

    @Query("""
            SELECT o
            FROM Order o
            WHERE o.product.seller.id = :sellerId
            """)
    List<Order> orderListBySeller(@Param("sellerId") Integer sellerId);

    @Query("""
            SELECT COUNT(*)
            FROM Order o
            WHERE o.product.id = :productId
            """)
    Integer countProduct(@Param("productId") Integer productId);

    @Query("""
            SELECT o
            FROM Order o
            WHERE (:seller IS NULL OR o.product.seller.id = :seller)
            AND (:buyer IS NULL OR o.buyer.id = :buyer)
            """)

    List<Order> findAllBySearch(@Param("seller") Integer seller,@Param("buyer") Integer buyer);
}
