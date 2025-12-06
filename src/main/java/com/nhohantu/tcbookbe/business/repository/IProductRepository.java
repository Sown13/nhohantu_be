package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.repository.BaseProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface IProductRepository extends BaseProductRepository {

    @Query("""
        SELECT DISTINCT p FROM ProductModel p
        LEFT JOIN p.productCategories pc
        LEFT JOIN pc.category c
        WHERE (:text IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :text, '%')))
          AND (:category IS NULL OR c.name = :category)
          AND (:active IS NULL OR p.active = :active)
          AND (:priceMin IS NULL OR p.price >= :priceMin)
          AND (:priceMax IS NULL OR p.price <= :priceMax)
    """)
    Page<ProductModel> searchProducts(
            @Param("text") String text,
            @Param("category") String category,
            @Param("active") Boolean active,
            @Param("priceMin") BigDecimal priceMin,
            @Param("priceMax") BigDecimal priceMax,
            Pageable pageable
    );

    Optional<ProductModel> findBySlug(String slug);
}