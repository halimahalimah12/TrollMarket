package com.TrollMarket.TrollMarket.repositories;

import com.TrollMarket.TrollMarket.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    @Query("""
            SELECT c
            FROM Category c
            WHERE (:name IS NULL OR c.name LIKE %:name%)
            """)
    Page<Category> findAll(Pageable pageable, @Param("name") String name);

}
