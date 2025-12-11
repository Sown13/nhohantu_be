package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.request.ReviewRequest;
import com.nhohantu.tcbookbe.business.dto.response.ReviewResponse;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.business.repository.IReviewRepository;
import com.nhohantu.tcbookbe.common.exception.NotFoundException;
import com.nhohantu.tcbookbe.common.model.builder.MetaData;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.entity.ReviewModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.utils.PagingValidationUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final IReviewRepository reviewRepository;
    private final IProductRepository productRepository;
    private final ModelMapper modelMapper;

    //Tạo review mới cho sản phẩm
    @Transactional
    public ResponseEntity<ResponseDTO<ReviewResponse>> createReview(@Valid ReviewRequest request) {
        try {
            // Kiểm tra sản phẩm có tồn tại không
            ProductModel product = productRepository.findById(request.getProductId())
                    .orElseThrow(
                            () -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + request.getProductId()));

            // Tạo title từ message nếu chưa có
            String title = request.getMessage().length() > 50
                    ? request.getMessage().substring(0, 50) + "..."
                    : request.getMessage();

            // Tạo ReviewModel từ request
            ReviewModel review = ReviewModel.builder()
                    .product(product)
                    .authorName(request.getName())
                    .authorEmail(request.getEmail())
                    .rating(request.getRating())
                    .title(title)
                    .description(request.getMessage())
                    .build();

            // Lưu vào database
            ReviewModel savedReview = reviewRepository.save(review);

            // Map sang response
            ReviewResponse response = mapToResponse(savedReview);

            return ResponseBuilder.okResponse(
                    "Tạo đánh giá thành công",
                    response,
                    StatusCodeEnum.SUCCESS2000);

        } catch (NotFoundException e) {
            log.error("Product not found: {}", e.getMessage());
            return ResponseBuilder.badRequestResponse(e.getMessage(), StatusCodeEnum.EXCEPTION0404);
        } catch (Exception e) {
            log.error("Error creating review", e);
            return ResponseBuilder.badRequestResponse(
                    "Có lỗi xảy ra khi tạo đánh giá",
                    StatusCodeEnum.ERRORCODE4000);
        }
    }

    //Lấy danh sách review của một sản phẩm (có phân trang)
    public ResponseEntity<ResponseDTO<List<ReviewResponse>>> getReviewsByProductId(
            Long productId,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection) {
        try {
            // Kiểm tra sản phẩm có tồn tại không
            if (!productRepository.existsById(productId)) {
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy sản phẩm với ID: " + productId,
                        StatusCodeEnum.EXCEPTION0404);
            }

            // Tạo pageable
            Pageable pageable = PagingValidationUtil.createPageable(
                    pageNumber, pageSize, sortBy, sortDirection);

            // Lấy danh sách review
            Page<ReviewModel> reviewPage = reviewRepository.findByProductId(productId, pageable);

            // Map sang response
            List<ReviewResponse> responses = reviewPage.getContent().stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            // Tạo metadata
            MetaData metaData = new MetaData(
                    reviewPage.getTotalPages(),
                    reviewPage.getNumber(),
                    reviewPage.getSize(),
                    (int) reviewPage.getTotalElements());

            return ResponseBuilder.okResponse(
                    "Lấy danh sách đánh giá thành công",
                    responses,
                    StatusCodeEnum.SUCCESS2000,
                    metaData);

        } catch (Exception e) {
            log.error("Error getting reviews for product: {}", productId, e);
            return ResponseBuilder.badRequestResponse(
                    "Có lỗi xảy ra khi lấy danh sách đánh giá",
                    StatusCodeEnum.ERRORCODE4000);
        }
    }

    //Lấy tất cả review của một sản phẩm (không phân trang)
    public ResponseEntity<ResponseDTO<List<ReviewResponse>>> getAllReviewsByProductId(Long productId) {
        try {
            // Kiểm tra sản phẩm có tồn tại không
            if (!productRepository.existsById(productId)) {
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy sản phẩm với ID: " + productId,
                        StatusCodeEnum.EXCEPTION0404);
            }

            // Lấy danh sách review
            List<ReviewModel> reviews = reviewRepository.findByProductId(productId);

            // Map sang response
            List<ReviewResponse> responses = reviews.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return ResponseBuilder.okResponse(
                    "Lấy danh sách đánh giá thành công",
                    responses,
                    StatusCodeEnum.SUCCESS2000);

        } catch (Exception e) {
            log.error("Error getting all reviews for product: {}", productId, e);
            return ResponseBuilder.badRequestResponse(
                    "Có lỗi xảy ra khi lấy danh sách đánh giá",
                    StatusCodeEnum.ERRORCODE4000);
        }
    }

    //Lấy thông tin thống kê review của sản phẩm
    public ResponseEntity<ResponseDTO<ReviewStatistics>> getReviewStatistics(Long productId) {
        try {
            // Kiểm tra sản phẩm có tồn tại không
            if (!productRepository.existsById(productId)) {
                return ResponseBuilder.badRequestResponse(
                        "Không tìm thấy sản phẩm với ID: " + productId,
                        StatusCodeEnum.EXCEPTION0404);
            }

            Long totalReviews = reviewRepository.countByProductId(productId);
            Double averageRating = reviewRepository.getAverageRatingByProductId(productId);

            ReviewStatistics statistics = ReviewStatistics.builder()
                    .productId(productId)
                    .totalReviews(totalReviews)
                    .averageRating(averageRating != null ? averageRating : 0.0)
                    .build();

            return ResponseBuilder.okResponse(
                    "Lấy thống kê đánh giá thành công",
                    statistics,
                    StatusCodeEnum.SUCCESS2000);

        } catch (Exception e) {
            log.error("Error getting review statistics for product: {}", productId, e);
            return ResponseBuilder.badRequestResponse(
                    "Có lỗi xảy ra khi lấy thống kê đánh giá",
                    StatusCodeEnum.ERRORCODE4000);
        }
    }

    //Map ReviewModel sang ReviewResponse
    private ReviewResponse mapToResponse(ReviewModel review) {
        ReviewResponse response = modelMapper.map(review, ReviewResponse.class);
        response.setProductId(review.getProduct().getId());
        response.setProductName(review.getProduct().getName());
        return response;
    }

    //Inner class cho thống kê review
    @lombok.Data
    @lombok.Builder
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class ReviewStatistics {
        private Long productId;
        private Long totalReviews;
        private Double averageRating;
    }
}
