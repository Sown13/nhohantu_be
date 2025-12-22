package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.business.dto.response.*;
import com.nhohantu.tcbookbe.business.repository.ICategoryRepository;
import com.nhohantu.tcbookbe.business.repository.IProductCategoryRepository;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.common.model.builder.MetaData;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductCategoryModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.utils.PagingValidationUtil;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
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

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> searchProducts(
            String text,
            String category,
            Boolean active,
            BigDecimal priceMin,
            BigDecimal priceMax,
            Integer pageNumber,
            Integer pageSize,
            String sortBy
    ) {
        try {
            Pageable pageable = PageRequest.of(pageNumber, pageSize, PagingValidationUtil.parseSort(sortBy));

            Page<ProductModel> result = productRepository.searchProducts(
                    text, category, active, priceMin, priceMax, pageable
            );

            List<GetProductListResponse> response = result.getContent()
                    .stream()
                    .map(this::mapToProductResponse)
                    .toList();

            return ResponseBuilder.okResponse(
                    "SUCCESS",
                    response,
                    StatusCodeEnum.SUCCESS2000,
                    new MetaData(result.getTotalPages(), result.getNumber(),
                            result.getSize(), result.getTotalElements())
            );

        } catch (Exception e) {
            log.error("Error while searching products", e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<ProductDetailResponse>> getProductBySlug(String slug) {
        Optional<ProductModel> productOpt = productRepository.findBySlug(slug);

        if (productOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Product not found", StatusCodeEnum.EXCEPTION0404);
        }

        ProductModel product = productOpt.get();


        // Map gallery
        List<AttachmentResponse> gallery = product.getProductImages().stream()
                .map(img -> new AttachmentResponse(img.getImageUrl(), img.getDescription()))
                .toList();

        // Map category: lấy category level 3 nếu có
        CategoryResponse category = product.getProductCategories().stream()
                .map(pc -> pc.getCategory())
                .filter(cat -> cat.getCategoryLevel() == 3)
                .findFirst()
                .map(cat -> new CategoryResponse(cat.getId(), cat.getName()))
                .orElse(null);

        // Map tags
        List<TagResponse> tags = product.getTags().stream()
                .map(tag -> new TagResponse(tag.getId(), tag.getName()))
                .toList();

        ProductDetailResponse response = new ProductDetailResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setSlug(product.getSlug());
        response.setPrice(product.getPrice());
        response.setQuantity(product.getQuantity());
        response.setSold(product.getSold());
        response.setVideoUrl(product.getVideoUrl());
        response.setUnit(product.getUnit());
        response.setSalePrice(product.getSalePrice());
        response.setMinPrice(product.getPrice()); // có thể tính min/max từ biến thể nếu có
        response.setMaxPrice(product.getPrice());
        response.setMainImageUrl(product.getMainImageUrl());
        response.setGallery(gallery);
        response.setCategory(category);
        response.setTag(tags);
        response.setBrand(product.getBrand());
        response.setDescription(product.getDescription());
        response.setVariations(product.getVariations());
        response.setRating(product.getRating());
        response.setDiscountPercentage(product.getDiscountPercentage());
        response.setWeight(product.getWeight());
        response.setActive(product.getActive());

        return ResponseBuilder.okResponse("Success", response, StatusCodeEnum.SUCCESS2000);
    }

    private GetProductListResponse mapToProductResponse(ProductModel product) {

        // Lấy category level 3 (FE cần category gốc)
        CategoryResponse category = null;
        if (!product.getProductCategories().isEmpty()) {
            ProductCategoryModel pc = product.getProductCategories().get(0);
            CategoryModel c = pc.getCategory();
            category = CategoryResponse.builder()
                    .id(c.getId())
                    .name(c.getName())
                    .parentId(c.getParentCategory() != null ? c.getParentCategory().getId() : null)
                    .categoryLevel(c.getCategoryLevel())
                    .build();
        }

        // Map gallery, tags nếu có (ví dụ placeholder)
        List<AttachmentResponse> gallery = new ArrayList<>();
        List<TagResponse> tags = new ArrayList<>();

        return GetProductListResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(generateSlug(product.getName()))
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .sold(0) // placeholder
                .videoUrl(null) // placeholder
                .unit("pcs") // placeholder
                .salePrice(null) // placeholder
                .minPrice(null)
                .maxPrice(null)
                .mainImageUrl(product.getMainImageUrl())
                .sku(null) // placeholder
                .gallery(gallery)
                .category(category)
                .tag(tags)
                .brand(null) // placeholder
                .description(product.getDescription())
                .variations(new HashMap<>()) // placeholder
                .rating(0f) // placeholder
                .discountPercentage(0f) // placeholder
                .weight(0f) // placeholder
                .active(product.getActive())
                .build();
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-+$", "");
    }

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getBestSellerProducts(Integer limit) {
        try {
            List<ProductModel> products = productRepository.findBestProductByOrderByRatingDesc()
                    .stream()
                    .limit(limit)
                    .toList();

            List<GetProductListResponse> response = products.stream()
                    .map(this::mapToProductResponse)
                    .toList();

            return ResponseBuilder.okResponse(
                    "SUCCESS",
                    response,
                    StatusCodeEnum.SUCCESS2000
            );
        } catch (Exception e) {
            log.error("Error while fetching best-seller products", e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getMostOrderedProducts(Integer limit) {
        try {
            List<ProductModel> products = productRepository.findMostOrderedProducts()
                    .stream()
                    .limit(limit)
                    .toList();

            List<GetProductListResponse> response = products.stream()
                    .map(this::mapToProductResponse)
                    .toList();

            return ResponseBuilder.okResponse(
                    "SUCCESS",
                    response,
                    StatusCodeEnum.SUCCESS2000
            );
        } catch (Exception e) {
            log.error("Error while fetching most ordered products", e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getRelatedProducts(String slug) {
        try {
            // 1. Get the product by slug
            Optional<ProductModel> productOpt = productRepository.findBySlug(slug);
            if (productOpt.isEmpty()) {
                return ResponseBuilder.badRequestResponse("Product not found", StatusCodeEnum.EXCEPTION0404);
            }

            ProductModel product = productOpt.get();

            // 2. Get category IDs of the product
            List<Long> categoryIds = product.getProductCategories().stream()
                    .map(pc -> pc.getCategory().getId())
                    .toList();

            if (categoryIds.isEmpty()) {
                // If no categories, return empty list
                return ResponseBuilder.okResponse(
                        "SUCCESS",
                        new ArrayList<>(),
                        StatusCodeEnum.SUCCESS2000
                );
            }

            // 3. Find related products that share the same categories, excluding current product
            List<ProductModel> relatedProducts = productRepository.findDistinctByProductCategories_Category_IdInAndIdNot(
                    categoryIds,
                    product.getId()
            );

            // 4. Map to DTO
            List<GetProductListResponse> response = relatedProducts.stream()
                    .map(this::mapToProductResponse)
                    .toList();

            return ResponseBuilder.okResponse("SUCCESS", response, StatusCodeEnum.SUCCESS2000);

        } catch (Exception e) {
            log.error("Error while fetching related products for slug: {}", slug, e);
            return ResponseBuilder.badRequestResponse("ERROR", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> clientSearchProducts(String text) {
        try {
            List<ProductModel> products = productRepository.clientSearchProducts(text);

            List<GetProductListResponse> response = products.stream()
                    .map(product -> GetProductListResponse.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .slug(product.getSlug())
                            .mainImageUrl(product.getMainImageUrl())
                            .build())
                    .toList();

            return ResponseBuilder.okResponse(
                    "Products fetched successfully",
                    response,
                    StatusCodeEnum.SUCCESS2000
            );
        } catch (Exception e) {
            log.error("Error while searching products", e);
            return ResponseBuilder.badRequestResponse(
                    "Error fetching products",
                    StatusCodeEnum.ERRORCODE4000
            );
        }
    }
}
