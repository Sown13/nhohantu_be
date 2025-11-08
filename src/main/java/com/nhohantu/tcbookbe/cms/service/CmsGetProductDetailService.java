package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.response.CmsGetProductDetailResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsGetProductDetailRepository;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductCategoryModel;
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

        List<CategoryModel> catList = productModel.getProductCategories().stream()
                .map(pc -> {
                    CategoryModel c = pc.getCategory();
                    return CategoryModel.builder()
                            .parentCategory(CategoryModel.builder().build())
                            .name(c.getName())
                            .build();
                })
                .toList();

        return CmsGetProductDetailResponse.builder()
                .id(productModel.getId())
                .name(productModel.getName())
                .price(productModel.getPrice())
                .active(productModel.getActive())
                .mainImageUrl(productModel.getMainImageUrl())
                .quantity(productModel.getQuantity())
                .description(productModel.getDescription())
                .categories(catList)
                .createdAt(productModel.getCreatedAt())
                .updatedAt(productModel.getUpdatedAt())
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
                        .createdAt(productModel.getCreatedAt())
                        .updatedAt(productModel.getUpdatedAt())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
