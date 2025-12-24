package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.request.ReviewRequest;
import com.nhohantu.tcbookbe.business.dto.response.ReviewResponse;
import com.nhohantu.tcbookbe.business.repository.IOrderRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.business.repository.IReviewRepository;
import com.nhohantu.tcbookbe.common.exception.NotFoundException;
import com.nhohantu.tcbookbe.common.model.builder.MetaData;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.OrderModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.entity.ReviewModel;
import com.nhohantu.tcbookbe.common.model.enums.OrderStatus;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.model.system.UserBasicInfoModel;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
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
    private final IOrderRepository orderRepository;
    private final UserBasicInfoService userBasicInfoService;
    private final ModelMapper modelMapper;

    //Tạo review mới cho sản phẩm
    @Transactional
    public ResponseEntity<ResponseDTO<ReviewResponse>> createReview(@Valid ReviewRequest request) {
        try {
            // 1. Auth Check: Kiểm tra user đang đăng nhập
            UserBasicInfoModel currentUser = userBasicInfoService.getUserInfoFromContext();
            if (currentUser == null) {
                return ResponseBuilder.badRequestResponse(
                        "Vui lòng đăng nhập để đánh giá sản phẩm",
                        StatusCodeEnum.EXCEPTION0505);
            }

            // 2. Kiểm tra đơn hàng có tồn tại không
            OrderModel order = orderRepository.findById(request.getOrderId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng với ID: " + request.getOrderId()));

            // 3. Ownership Check: Đơn hàng này có đúng là của người đang login không?
            if (!order.getUser().getId().equals(currentUser.getId())) {
                return ResponseBuilder.badRequestResponse(
                        "Bạn không có quyền đánh giá đơn hàng này",
                        StatusCodeEnum.EXCEPTION0505);
            }

            // 4. Status Check: Đơn hàng đã ở trạng thái DELIVERED chưa?
            if (order.getStatus() != OrderStatus.DELIVERED) {
                return ResponseBuilder.badRequestResponse(
                        "Chỉ có thể đánh giá sản phẩm khi đơn hàng đã được giao (DELIVERED)",
                        StatusCodeEnum.ERRORCODE4000);
            }

            // 5. Kiểm tra sản phẩm có tồn tại không
            ProductModel product = productRepository.findById(request.getProductId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy sản phẩm với ID: " + request.getProductId()));

            // 6. Kiểm tra sản phẩm có trong đơn hàng không
            boolean isProductInOrder = order.getOrderDetails().stream()
                    .anyMatch(detail -> detail.getProduct().getId().equals(request.getProductId()));
            if (!isProductInOrder) {
                return ResponseBuilder.badRequestResponse(
                        "Sản phẩm này không có trong đơn hàng của bạn",
                        StatusCodeEnum.ERRORCODE4000);
            }

            // 7. Duplicate Check: order_id này đã đánh giá sản phẩm này chưa?
            boolean isAlreadyReviewed = reviewRepository.existsByOrderIdAndProductId(
                    request.getOrderId(), request.getProductId());
            if (isAlreadyReviewed) {
                return ResponseBuilder.badRequestResponse(
                        "Bạn đã đánh giá sản phẩm này trong đơn hàng này rồi",
                        StatusCodeEnum.ERRORCODE4000);
            }

            // Tạo title từ message nếu chưa có
            String title = request.getMessage().length() > 50
                    ? request.getMessage().substring(0, 50) + "..."
                    : request.getMessage();

            // Tạo ReviewModel từ request
            ReviewModel review = ReviewModel.builder()
                    .order(order)
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
            log.error("Not found: {}", e.getMessage());
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
        response.setOrderId(review.getOrder().getId());
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
