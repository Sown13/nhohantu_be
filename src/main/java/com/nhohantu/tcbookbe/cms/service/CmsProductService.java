package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateProductRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateProductResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsCategoryRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsProductCategoryRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsProductRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsTagRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.*;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import com.nhohantu.tcbookbe.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CmsProductService {
    private final UserBasicInfoService userBasicInfoService;
    private final ModelMapper mapper;
    private final ICmsProductRepository productRepository;
    private final ICmsCategoryRepository categoryRepository;
    private final ICmsProductCategoryRepository productCategoryRepository;
    private final ICmsTagRepository tagRepository;

    public ResponseEntity<ResponseDTO<CmsCreateProductResponse>> createProduct(CmsCreateProductRequest request) {
        if (request.getCategoryIds() == null || request.getCategoryIds().isEmpty()) {
            throw new IllegalArgumentException("Sản phẩm phải thuộc ít nhất 1 category");
        }

        //lấy thông tin user đăng nhập để set 1 số info//todo tạm để tiện test
//        UserBasicInfoModel currentUser = userBasicInfoService.getUserInfoFromContext();
//        if (currentUser != null) {
//            return ResponseBuilder.badRequestResponse("Tạo sản phẩm thất bại. Lỗi khi lấy thông tin user đăng nhập",
//                    StatusCodeEnum.ERRORCODE4000);
//        }

        // Lấy category
        List<CategoryModel> categories = categoryRepository.findAllById(request.getCategoryIds());
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("Category không hợp lệ");
        }

        // Generate slug từ tên
        String slug = generateSlug(request.getName());

        // Build product
        ProductModel product = ProductModel.builder()
                .name(request.getName())
                .slug(slug)
                .description(request.getDescription())
                .price(request.getPrice())
                .salePrice(request.getSalePrice())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .sold(0) // mới tạo = 0
                .active(request.getActive() != null ? request.getActive() : false)
                .mainImageUrl(request.getMainImageUrl() != null && !request.getMainImageUrl().isEmpty() ? request.getMainImageUrl() : Constant.DEFAULT_IMAGE_URL)
                .videoUrl(request.getVideoUrl())
                .unit(request.getUnit())
                .sku(request.getSku())
                .brand(request.getBrand())
                .weight(request.getWeight())
                .discountPercentage(request.getDiscountPercentage())
                .rating(request.getRating())
                .variations(request.getVariations() != null ? request.getVariations() : new HashMap<>())
                .build();

        // Map category
        List<ProductCategoryModel> productCategories = categories.stream().map(cat ->
                ProductCategoryModel.builder()
                        .product(product)
                        .category(cat)
                        .build()
        ).toList();
        product.setProductCategories(productCategories);

        // Map gallery nếu request có
        if (request.getGallery() != null && !request.getGallery().isEmpty()) {
            List<ProductImageModel> images = request.getGallery().stream().map(imgReq ->
                    ProductImageModel.builder()
                            .product(product)
                            .imageUrl(imgReq.getUrl())
                            .description(imgReq.getName())
                            .isPrimary(false) // mặc định không phải ảnh chính
                            .build()
            ).toList();
            // nếu FE muốn ảnh chính thì set isPrimary = true ở 1 ảnh
            product.setProductImages(images);
        }

        // Map tags nếu có
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<TagModel> tags = tagRepository.findAllById(request.getTagIds());
            product.setTags(tags);
        }

        try {
            ProductModel result = productRepository.save(product);
            CmsCreateProductResponse response = mapper.map(result, CmsCreateProductResponse.class);

            return ResponseBuilder.okResponse("Tạo sản phẩm thành công", response, StatusCodeEnum.SUCCESS2000);
        } catch (Exception e) {
            log.error("Lỗi khi tạo sản phẩm", e);
            return ResponseBuilder.badRequestResponse("Tạo sản phẩm thất bại. Lỗi khi tạo sản phẩm",
                    StatusCodeEnum.ERRORCODE4000);
        }
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("-+$", "");
    }


//    public ResponseBuilder<ResponseDTO<CmsCreateProductResponse>> getProduct(Long id) {
//        ProductModel product = productRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
//        List<ProductCategoryModel> productCategories = productCategoryRepository.findAll()
//                .stream().filter(pc -> pc.getProduct().getId().equals(id)).toList();
//        List<CategoryModel> categories = productCategories.stream().map(ProductCategoryModel::getCategory).toList();
//        return toResponse(product, categories);
//    }
}
