package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.response.GetProductDetailResponse;
import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.business.repository.ICategoryRepository;
import com.nhohantu.tcbookbe.business.repository.IProductCategoryRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.common.model.builder.MetaData;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.utils.PagingValidationUtil;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@CustomLog
@Service
@AllArgsConstructor
public class ProductService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IProductCategoryRepository productCategoryRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getProducts(
            Integer pageNumber, Integer pageSize, String sortBy, String sortDirection) {

        try {
            Pageable pageable = PagingValidationUtil.createPageable(pageNumber, pageSize, sortBy, sortDirection);

            //todo giai đoạn phát triển dùng mặc định hết cho nhanh, sau này cần sẽ tối ưu những chỗ này để tránh N+1 query
            Page<ProductModel> result = productRepository.findAll(pageable);

            List<GetProductListResponse> productListResponse = result.getContent()
                    .stream()
                    .map(product -> modelMapper.map(product, GetProductListResponse.class))
                    .toList();

            return ResponseBuilder.okResponse(
                    "SUCCESS",
                    productListResponse,
                    StatusCodeEnum.SUCCESS2000,
                    new MetaData(result.getTotalPages(), result.getNumber(),
                            result.getSize(), result.getTotalElements())
            );
        } catch (Exception e) {
            log.error("Error while fetching products", e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<GetProductDetailResponse>> getProductDetailById(Long productId) {
        try {
            Optional<ProductModel> productOpt = productRepository.findById(productId);

            if (productOpt.isEmpty()) {
                return ResponseBuilder.badRequestResponse(
                        "Product not found with id: " + productId,
                        StatusCodeEnum.EXCEPTION0404
                );
            }

            GetProductDetailResponse productDetailResponse =
                    modelMapper.map(productOpt.get(), GetProductDetailResponse.class);

            return ResponseBuilder.okResponse(
                    "SUCCESS",
                    productDetailResponse,
                    StatusCodeEnum.SUCCESS2000
            );

        } catch (Exception e) {
            log.error("Error while fetching product detail for id: " + productId, e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

}
