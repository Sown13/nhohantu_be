package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateProductRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateProductResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsProductPageResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsCategoryRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsProductCategoryRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsProductRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsTagRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.*;
import com.nhohantu.tcbookbe.common.model.entity.TagModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import com.nhohantu.tcbookbe.common.utils.Constant;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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

    @Transactional
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
        if (request.getTagIds() != null && !request.getTagIds().isEmpty()) {
            List<TagModel> tags = tagRepository.findAllById(request.getTagIds());
            product.setTags(tags);
        }

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

//        product.setSlug("");

        try {
            ProductModel result = productRepository.save(product);

            List<Long> tagIds = result.getTags().stream()
                    .map(TagModel::getId)
                    .collect(Collectors.toList());

            CmsCreateProductResponse response = CmsCreateProductResponse.builder()
                    .id(result.getId())
                    .name(result.getName())
                    .description(result.getDescription())
                    .price(result.getPrice())
                    .quantity(result.getQuantity())
                    .active(result.getActive())
                    .mainImageUrl(result.getMainImageUrl())
                    .categories(categories)
                    .tagIds(tagIds)
                    .build();

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

    @Transactional
    public CmsCreateProductResponse updateProduct(Long id, CmsCreateProductRequest req) {

        ProductModel product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // === scalar fields ===
        product.setName(req.getName());
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setSalePrice(req.getSalePrice());
        product.setQuantity(req.getQuantity());
        product.setActive(req.getActive());
        product.setMainImageUrl(req.getMainImageUrl());
        product.setVideoUrl(req.getVideoUrl());
        product.setUnit(req.getUnit());
        product.setSku(req.getSku());
        product.setBrand(req.getBrand());
        product.setWeight(req.getWeight());
        product.setDiscountPercentage(req.getDiscountPercentage());
        product.setRating(req.getRating());
        product.setVariations(req.getVariations());

        // === categories ===
        if (req.getCategoryIds() != null) {
            product.getProductCategories().clear();

            List<CategoryModel> categories = categoryRepository.findAllById(req.getCategoryIds());
            categories.forEach(cat -> {
                ProductCategoryModel pcm = new ProductCategoryModel();
                pcm.setProduct(product);
                pcm.setCategory(cat);
                product.getProductCategories().add(pcm);
            });
        }

        // === tags ===
        if (req.getTagIds() != null) {
            List<TagModel> tags = tagRepository.findAllById(req.getTagIds());
            product.setTags(tags);
        }
        ProductModel saved = productRepository.save(product);

        // === map sang DTO ===
        return CmsCreateProductResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .description(saved.getDescription())
                .price(saved.getPrice())
//                .salePrice(saved.getSalePrice())
                .quantity(saved.getQuantity())
                .active(saved.getActive())
                .mainImageUrl(saved.getMainImageUrl())
//                .videoUrl(saved.getVideoUrl())
//                .unit(saved.getUnit())
//                .sku(saved.getSku())
//                .brand(saved.getBrand())
//                .weight(saved.getWeight())
//                .discountPercentage(saved.getDiscountPercentage())
//                .rating(saved.getRating())
//                .variations(saved.getVariations())
//                .categoryIds(saved.getProductCategories().stream()
//                        .map(pc -> pc.getCategory().getId())
//                        .toList())
                .tagIds(saved.getTags().stream()
                        .map(TagModel::getId)
                        .toList())
                .build();
    }

    public void deleteProduct(Long id) {
        ProductModel productModel = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Id not found for delete"));

         productRepository.delete(productModel);
    }

    @Transactional
    public CmsProductPageResponse getProductsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending()); // sắp xếp theo id giảm dần
        Page<ProductModel> productPage = productRepository.findAll(pageable);

        List<CmsCreateProductResponse> products = productPage.getContent().stream()
                .map(p -> CmsCreateProductResponse.builder()
                        .id(p.getId())
                        .name(p.getName())
                        .description(p.getDescription())
                        .price(p.getPrice())
                        .quantity(p.getQuantity())
                        .active(p.getActive())
                        .mainImageUrl(p.getMainImageUrl())
                        .tagIds(p.getTags().stream().map(TagModel::getId).toList())
                        .build())
                .toList();

        CmsProductPageResponse response = new CmsProductPageResponse();
        response.setContent(products);
        response.setPageNumber(productPage.getNumber());
        response.setPageSize(productPage.getSize());
        response.setTotalElements(productPage.getTotalElements());
        response.setTotalPages(productPage.getTotalPages());
        response.setLast(productPage.isLast());

        return response;
    }
}
