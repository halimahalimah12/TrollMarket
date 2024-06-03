package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Buyer;
import com.TrollMarket.TrollMarket.models.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {

    @Query("""
            SELECT b
            FROM Buyer b
            WHERE b.account.username LIKE %:username%
            """)
    Buyer buyerFindByAccound(@Param("username") String username);
}
