package com.nhohantu.tcbookbe.business.repository;

import com.nhohantu.tcbookbe.common.model.entity.ReviewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<ReviewModel, Long> {

    //Kiểm tra đã tồn tại review cho order và product chưa (Duplicate Check)
    @Query("SELECT COUNT(r) > 0 FROM ReviewModel r WHERE r.order.id = :orderId AND r.product.id = :productId AND r.isDeleted = false")
    boolean existsByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

    //Tìm tất cả review của một sản phẩm (có phân trang)
    @Query("SELECT r FROM ReviewModel r WHERE r.product.id = :productId AND r.isDeleted = false ORDER BY r.createdAt DESC")
    Page<ReviewModel> findByProductId(@Param("productId") Long productId, Pageable pageable);

    //Tìm tất cả review của một sản phẩm (không phân trang)
    @Query("SELECT r FROM ReviewModel r WHERE r.product.id = :productId AND r.isDeleted = false ORDER BY r.createdAt DESC")
    List<ReviewModel> findByProductId(@Param("productId") Long productId);

    //Đếm số lượng review của một sản phẩm
    @Query("SELECT COUNT(r) FROM ReviewModel r WHERE r.product.id = :productId AND r.isDeleted = false")
    Long countByProductId(@Param("productId") Long productId);

    //Tính rating trung bình của một sản phẩm
    @Query("SELECT AVG(r.rating) FROM ReviewModel r WHERE r.product.id = :productId AND r.isDeleted = false")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
}
