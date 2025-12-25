package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.request.ReviewRequest;
import com.nhohantu.tcbookbe.business.dto.response.CanReviewResponse;
import com.nhohantu.tcbookbe.business.dto.response.ReviewResponse;
import com.nhohantu.tcbookbe.business.service.ReviewService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * API kiểm tra user có thể review sản phẩm này không
     * GET /reviews/can-review?productId=1
     */
    @GetMapping("/can-review")
    public ResponseEntity<ResponseDTO<CanReviewResponse>> canReview(@RequestParam Long productId) {
        return reviewService.canReview(productId);
    }

    /**
     * API tạo đánh giá mới cho sản phẩm
     */
    @PostMapping
    public ResponseEntity<ResponseDTO<ReviewResponse>> createReview(@Valid @RequestBody ReviewRequest request) {
        return reviewService.createReview(request);
    }

    /**
     * API lấy danh sách đánh giá của sản phẩm (có phân trang)
     * GET /reviews/product/{productId}
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<ResponseDTO<List<ReviewResponse>>> getReviewsByProductId(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        return reviewService.getReviewsByProductId(productId, pageNumber, pageSize, sortBy, sortDirection);
    }

    /**
     * API lấy tất cả đánh giá của sản phẩm (không phân trang)
     * GET /reviews/product/{productId}/all
     */
    @GetMapping("/product/{productId}/all")
    public ResponseEntity<ResponseDTO<List<ReviewResponse>>> getAllReviewsByProductId(@PathVariable Long productId) {
        return reviewService.getAllReviewsByProductId(productId);
    }

    /**
     * API lấy thông tin thống kê đánh giá của sản phẩm
     * GET /reviews/product/{productId}/statistics
     */
    @GetMapping("/product/{productId}/statistics")
    public ResponseEntity<ResponseDTO<ReviewService.ReviewStatistics>> getReviewStatistics(
            @PathVariable Long productId) {
        return reviewService.getReviewStatistics(productId);
    }
}
