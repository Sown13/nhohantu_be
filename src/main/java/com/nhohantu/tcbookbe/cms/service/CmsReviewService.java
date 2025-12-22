package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.response.CmsReviewListResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsReviewRepository;
import com.nhohantu.tcbookbe.common.model.builder.MetaData;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.entity.ReviewModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.utils.PagingValidationUtil;
import lombok.CustomLog;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@CustomLog
public class CmsReviewService {

    private final ICmsReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<ResponseDTO<List<CmsReviewListResponse>>> getReviews(
            Long reviewId,
            Long productId,
            String productName,
            String text,
            Integer rating,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection
    ) {
        try {
            Pageable pageable = PagingValidationUtil.createPageable(pageNumber, pageSize, sortBy, sortDirection);

            Page<ReviewModel> page = reviewRepository.cmsSearchReviews(
                    reviewId,
                    productId,
                    productName,
                    text,
                    rating,
                    pageable
            );

            List<CmsReviewListResponse> data = page.getContent().stream()
                    .map(this::mapToCmsListResponse)
                    .toList();

            return ResponseBuilder.okResponse(
                    "SUCCESS",
                    data,
                    StatusCodeEnum.SUCCESS2000,
                    new MetaData(page.getTotalPages(), page.getNumber(), page.getSize(), page.getTotalElements())
            );
        } catch (Exception e) {
            log.error("Error while cms searching reviews", e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<Void>> deleteReview(Long id) {
        try {
            ReviewModel review = reviewRepository.findById(id)
                    .orElse(null);

            if (review == null) {
                return ResponseBuilder.badRequestResponse("Review not found", StatusCodeEnum.EXCEPTION0404);
            }

            reviewRepository.delete(review);

            return ResponseBuilder.okResponse("Deleted successfully", null, StatusCodeEnum.SUCCESS2000);
        } catch (Exception e) {
            log.error("Error while deleting review id={}", id, e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    private CmsReviewListResponse mapToCmsListResponse(ReviewModel reviewModel) {
        ProductModel p = reviewModel.getProduct();

        return CmsReviewListResponse.builder()
                .id(reviewModel.getId())

                // product
                .productId(p != null ? p.getId() : null)
                .productName(p != null ? p.getName() : null)
                .productSlug(p != null ? p.getSlug() : null)

                // author
                .authorName(reviewModel.getAuthorName())
                .authorEmail(reviewModel.getAuthorEmail())

                // review
                .rating(reviewModel.getRating())
                .title(reviewModel.getTitle())
                .description(reviewModel.getDescription())

                // audit
                .createdAt(reviewModel.getCreatedAt())
                .build();
    }

}
