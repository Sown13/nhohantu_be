package com.nhohantu.tcbookbe.cms.repository;

import com.nhohantu.tcbookbe.common.model.entity.ReviewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ICmsReviewRepository extends JpaRepository<ReviewModel, Long> {

    @Query("""
    select r
    from ReviewModel r
    join r.product p
    where (:reviewId is null or r.id = :reviewId)
      and (:productId is null or p.id = :productId)
      and (:productName is null or lower(p.name) like lower(concat('%', :productName, '%')))
      and (:text is null or lower(r.description) like lower(concat('%', :text, '%')))
      and (:rating is null or r.rating = :rating)
    """)
    Page<ReviewModel> cmsSearchReviews(
            @Param("reviewId") Long reviewId,
            @Param("productId") Long productId,
            @Param("productName") String productName,
            @Param("text") String text,
            @Param("rating") Integer rating,
            Pageable pageable
    );

}
