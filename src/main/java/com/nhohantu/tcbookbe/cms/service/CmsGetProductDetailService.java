package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.response.CmsGetProductDetailResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsGetProductDetailRepository;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CmsGetProductDetailService {
    private final ICmsGetProductDetailRepository cmsGetProductDetailRepository;

    public CmsGetProductDetailResponse getProductDetailById(Long id) {
        ProductModel productModel = cmsGetProductDetailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found id"));

        return CmsGetProductDetailResponse.builder()
                .id(productModel.getId())
                .name(productModel.getName())
                .price(productModel.getPrice())
                .active(productModel.getActive())
                .mainImageUrl(productModel.getMainImageUrl())
                .quantity(productModel.getQuantity())
                .description(productModel.getDescription())
                .build();
    }

    public List<CmsGetProductDetailResponse> getAllProducts() {
        List<ProductModel> productModels = cmsGetProductDetailRepository.findAll();

        return productModels.stream()
                .map(productModel -> CmsGetProductDetailResponse.builder()
                        .id(productModel.getId())
                        .name(productModel.getName())
                        .price(productModel.getPrice())
                        .active(productModel.getActive())
                        .mainImageUrl(productModel.getMainImageUrl())
                        .quantity(productModel.getQuantity())
                        .description(productModel.getDescription())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
