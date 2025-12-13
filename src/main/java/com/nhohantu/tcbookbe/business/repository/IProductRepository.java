package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.repository.BaseProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
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

    List<ProductModel> findBestProductByOrderByRatingDesc();

    @Query("""
        SELECT p
        FROM ProductModel p
        LEFT JOIN OrderDetailModel od ON od.product = p
        GROUP BY p
        ORDER BY COUNT(od.id) DESC
    """)
    List<ProductModel> findMostOrderedProducts();

    @Query("""
        SELECT DISTINCT p
        FROM ProductModel p
        JOIN p.productCategories pc
        JOIN pc.category c
        WHERE c.id = :categoryId
    """)
    List<ProductModel> findProductsByCategoryId(@Param("categoryId") Long categoryId);

    // Related products query
    List<ProductModel> findDistinctByProductCategories_Category_IdInAndIdNot(List<Long> categoryIds, Long excludeId);

    @Query("""
        SELECT DISTINCT p
        FROM ProductModel p
        JOIN p.productCategories pc
        JOIN pc.category c
        WHERE c.id IN :categoryIds
    """)
    List<ProductModel> findProductsByCategoryIds(@Param("categoryIds") List<Long> categoryIds);
}
