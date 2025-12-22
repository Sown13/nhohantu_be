package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.response.CmsReviewListResponse;
import com.nhohantu.tcbookbe.cms.service.CmsReviewService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cms/reviews")
@RequiredArgsConstructor
public class CmsReviewController {

    private final CmsReviewService cmsReviewService;

    /**
     * CMS: lấy danh sách feedback (có phân trang + filter)
     * GET /cms/reviews?productId=&productName=&reviewId=&text=&rating=&pageNumber=&pageSize=&sortBy=&sortDirection=
     */
    @GetMapping
    public ResponseEntity<ResponseDTO<List<CmsReviewListResponse>>> getReviews(
            @RequestParam(required = false) Long reviewId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) Integer rating,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        return cmsReviewService.getReviews(reviewId, productId, productName, text, rating,
                pageNumber, pageSize, sortBy, sortDirection);
    }

    /**
     * CMS: xoá feedback
     * DELETE /cms/reviews/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteReview(@PathVariable Long id) {
        return cmsReviewService.deleteReview(id);
    }
}
