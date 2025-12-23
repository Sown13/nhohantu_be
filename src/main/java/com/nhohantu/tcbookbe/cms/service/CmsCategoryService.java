package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.request.CmsUpdateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsCategoryRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.utils.Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CmsCategoryService {
    private final ICmsCategoryRepository categoryRepository;
    private final ModelMapper mapper;
    private final IProductRepository productRepository;

    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> createCategory(CmsCreateCategoryRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            return ResponseBuilder.badRequestResponse("Tên danh mục không được trống", StatusCodeEnum.ERRORCODE4000);
        }

        if (request.getCategoryLevel() == null) {
            return ResponseBuilder.badRequestResponse("Cấp độ danh mục không được trống", StatusCodeEnum.ERRORCODE4000);
        }

        if (request.getCategoryLevel() == 1) {
            if (request.getParentId() != null) {
                return ResponseBuilder.badRequestResponse("Danh mục cấp 1 không được có danh mục cha", StatusCodeEnum.ERRORCODE4000);
            }
        } else if (request.getCategoryLevel() > 1 && request.getCategoryLevel() <= 3) {
            if (request.getParentId() == null) {
                return ResponseBuilder.badRequestResponse("Danh mục cấp 2 trở lên phải có danh mục cha", StatusCodeEnum.ERRORCODE4000);
            }
        } else {
            return ResponseBuilder.badRequestResponse("Cấp độ danh mục không hợp lệ", StatusCodeEnum.ERRORCODE4000);
        }

        try {
            CategoryModel parentCategory = null;
            Integer categoryLevel = request.getCategoryLevel();

            if (request.getParentId() != null) {
                Optional<CategoryModel> parentOptional = categoryRepository.findById(request.getParentId());
                if (parentOptional.isEmpty()) {
                    return ResponseBuilder.badRequestResponse("Danh mục cha không tồn tại", StatusCodeEnum.ERRORCODE4000);
                }

                parentCategory = parentOptional.get();
                if (parentCategory.getCategoryLevel() + 1 != categoryLevel) {
                    return ResponseBuilder.badRequestResponse("Cấp độ danh mục không hợp lệ so với danh mục cha", StatusCodeEnum.ERRORCODE4000);
                }
                if (parentCategory.getCategoryLevel() == 3) {
                    return ResponseBuilder.badRequestResponse("Không thể tạo danh mục con cho danh mục cấp 3", StatusCodeEnum.ERRORCODE4000);
                }
            }

            String slug = Util.generateSlug(request.getName());

            //check trùng slug
            if (existsBySlugAndLevel(slug, categoryLevel)) {
                return ResponseBuilder.badRequestResponse(
                        "Slug danh mục đã tồn tại",
                        StatusCodeEnum.ERRORCODE4000
                );
            }
            CategoryModel category = CategoryModel.builder()
                    .name(request.getName())
                    .parentCategory(parentCategory)
                    .slug(slug)
                    .categoryLevel(categoryLevel).build();

            CategoryModel result = categoryRepository.save(category);
            CmsCreateCategoryResponse response = mapper.map(result, CmsCreateCategoryResponse.class);

            return ResponseBuilder.okResponse("Tạo danh mục thành công", response, StatusCodeEnum.SUCCESS2000);
        } catch (IllegalArgumentException e) {
            log.error("Lỗi khi tạo danh mục: " + e.getMessage(), e);
            return ResponseBuilder.badRequestResponse(e.getMessage(), StatusCodeEnum.ERRORCODE4000);
        } catch (Exception e) {
            log.error("Lỗi khi tạo danh mục: " + e.getMessage(), e);
            return ResponseBuilder.badRequestResponse("Tạo danh mục thất bại. Lỗi khi tạo danh mục", StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<List<CmsCreateCategoryResponse>>> findAllCategoryLevel3() {
        try {
            List<CategoryModel> categories = categoryRepository.findByCategoryLevel(3);

            List<CmsCreateCategoryResponse> responseList = categories.stream().map(category -> mapper.map(category, CmsCreateCategoryResponse.class)).collect(Collectors.toList());

            return ResponseBuilder.okResponse("Lấy danh sách danh mục cấp 3 thành công", responseList, StatusCodeEnum.SUCCESS2000);

        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(e.getMessage(), StatusCodeEnum.ERRORCODE4000);
        }
    }

    public ResponseEntity<ResponseDTO<List<CmsListCategoryResponse>>> findAllCategory() {
        try {
            List<CategoryModel> allCategories = categoryRepository.findAll();

            Map<Long, CmsListCategoryResponse> categoryMap = allCategories.stream()
                    .map(category -> {
                        CmsListCategoryResponse dto = mapper.map(category, CmsListCategoryResponse.class);

                        long productCount = category.getProductCategories() != null ? category.getProductCategories().size() : 0L;
                        dto.setProductCount(productCount);

                        return dto;
                    })
                    .collect(Collectors.toMap(CmsListCategoryResponse::getId, c -> c));

            // Build the category tree
            List<CmsListCategoryResponse> rootCategories = new ArrayList<>();
            for (CmsListCategoryResponse category : categoryMap.values()) {
                if (category.getParentId() != null) {
                    CmsListCategoryResponse parent = categoryMap.get(category.getParentId());
                    if (parent != null) {
                        if (parent.getChildren() == null) {
                            parent.setChildren(new ArrayList<>());
                        }
                        parent.getChildren().add(category);
                    }
                } else {
                    rootCategories.add(category);
                }
            }

            return ResponseBuilder.okResponse(
                    "Lấy danh sách danh mục thành công",
                    rootCategories,
                    StatusCodeEnum.SUCCESS2000
            );
        } catch (Exception e) {
            log.error("Error fetching categories: {}", e.getMessage(), e);
            return ResponseBuilder.badRequestResponse(
                    e.getMessage(),
                    StatusCodeEnum.ERRORCODE4000
            );
        }
    }

    public ResponseEntity<ResponseDTO<List<CmsListCategoryResponse>>> findCategoryProducts() {
        try {
            List<CategoryModel> allCategories = new ArrayList<>(
                    categoryRepository.findAll().stream()
                            .filter(c -> c.getCategoryLevel() != null)
                            .toList()
            );

            if (allCategories.isEmpty()) {
                return ResponseBuilder.okResponse(
                        "Không có danh mục nào có sản phẩm",
                        List.of(),
                        StatusCodeEnum.SUCCESS2000
                );
            }

            Collections.shuffle(allCategories);
            List<CategoryModel> randomCategories = allCategories.stream()
                    .limit(3)
                    .toList();

            List<CmsListCategoryResponse> categoryResponses = randomCategories.stream()
                    .map(category -> {
                        List<ProductModel> products = new ArrayList<>(productRepository.findProductsByCategoryId(category.getId()));
                        Collections.shuffle(products);
                        List<GetProductListResponse> productResponses = products.stream()
                                .limit(6)
                                .map(this::mapToProductResponse)
                                .toList();

                        return CmsListCategoryResponse.builder()
                                .id(category.getId())
                                .name(category.getName())
                                .slug(category.getSlug())
                                .imageUrl(category.getImageUrl())
                                .parentId(category.getParentCategory() != null ? category.getParentCategory().getId() : null)
                                .categoryLevel(category.getCategoryLevel())
                                .productCount((long) products.size())
                                .products(productResponses)
                                .build();
                    })
                    .toList();

            return ResponseBuilder.okResponse(
                    "Lấy danh sách danh mục thành công",
                    categoryResponses,
                    StatusCodeEnum.SUCCESS2000
            );

        } catch (Exception e) {
            log.error("Error fetching categories: {}", e.getMessage(), e);
            return ResponseBuilder.badRequestResponse(
                    e.getMessage(),
                    StatusCodeEnum.ERRORCODE4000
            );
        }
    }

    private GetProductListResponse mapToProductResponse(ProductModel product) {
        return GetProductListResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .price(product.getPrice())
                .mainImageUrl(product.getMainImageUrl())
                .salePrice(product.getSalePrice())
                .brand(product.getBrand())
                .rating(product.getRating())
                .discountPercentage(product.getDiscountPercentage())
                .quantity(product.getQuantity())
                .sold(product.getSold())
                .unit(product.getUnit())
                .build();
    }

    public List<Long> getAllCategoryIds(CategoryModel category) {
        List<Long> ids = new ArrayList<>();
        ids.add(category.getId());

        if (category.getChildCategories() != null) {
            for (CategoryModel child : category.getChildCategories()) {
                ids.addAll(getAllCategoryIds(child));
            }
        }

        return ids;
    }

    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getProductsByCategorySlug(
            String slug,
            String sortBy,
            Integer min,
            Integer max,
            Boolean onSale
    ) {
        // 1. Find category by slug
        Optional<CategoryModel> categoryOpt = categoryRepository.findBySlug(slug);
        if (categoryOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Category not found", StatusCodeEnum.EXCEPTION0404);
        }
        CategoryModel category = categoryOpt.get();

        // 2. Get all category IDs (parent + children)
        List<Long> categoryIds = getAllCategoryIds(category);

        // 3. Fetch products
        List<ProductModel> products = productRepository.findProductsByCategoryIds(categoryIds);
        if (products == null) products = new ArrayList<>();

        // 4. Set default filter values
        int minPrice = (min != null) ? min : 0;
        int maxPrice = (max != null) ? max : 50000;
        boolean filterOnSale = (onSale != null) ? onSale : false;
        String sortOption = (sortBy != null) ? sortBy : "new-arrival";

        // 5. Filter products
        List<ProductModel> filteredProducts = products.stream()
                .filter(p -> {
                    BigDecimal effectivePrice = getEffectivePrice(p);
                    return effectivePrice.compareTo(BigDecimal.valueOf(minPrice)) >= 0 &&
                            effectivePrice.compareTo(BigDecimal.valueOf(maxPrice)) <= 0;
                })
                .filter(p -> !filterOnSale || (p.getSalePrice() != null && p.getSalePrice().compareTo(p.getPrice()) < 0))
                .toList();

        // 6. Sort products (null-safe)
        List<ProductModel> sortedProducts = switch (sortOption) {
            case "lowest" -> filteredProducts.stream()
                    .sorted(Comparator.comparing(
                            IProductRepository::getEffectivePrice,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    ))
                    .toList();

            case "highest" -> filteredProducts.stream()
                    .sorted(Comparator.comparing(
                            IProductRepository::getEffectivePrice,
                            Comparator.nullsLast(Comparator.naturalOrder())
                    ).reversed())
                    .toList();

            case "best-selling" -> filteredProducts.stream()
                    .sorted(Comparator.comparing(
                            p -> p.getSold() != null ? p.getSold() : 0,
                            Comparator.reverseOrder()
                    ))
                    .toList();

            case "new-arrival" -> filteredProducts.stream()
                    .sorted(Comparator.comparing(
                            p -> p.getCreatedAt() != null ? p.getCreatedAt() : LocalDateTime.of(1970, 1, 1, 0, 0),
                            Comparator.reverseOrder()
                    ))
                    .toList();

            default -> filteredProducts;
        };

        // 7. Map to DTO
        List<GetProductListResponse> response = sortedProducts.stream()
                .map(this::mapToProductResponse)
                .toList();

        return ResponseBuilder.okResponse("SUCCESS", response, StatusCodeEnum.SUCCESS2000);
    }

    // --- Helper method ---
    private static BigDecimal getEffectivePrice(ProductModel product) {
        if (product == null) return BigDecimal.ZERO;
        if (product.getSalePrice() != null && product.getSalePrice().compareTo(BigDecimal.ZERO) > 0) {
            return product.getSalePrice();
        }
        return product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
    }

    public boolean existsBySlugAndLevel(String slug, Integer level) {
        return categoryRepository.existsBySlugAndCategoryLevel(slug, level);
    }

    @Transactional
    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> updateCategory(CmsUpdateCategoryRequest request) {
        if (request == null || request.getId() == null) {
            return ResponseBuilder.badRequestResponse("Thiếu id danh mục", StatusCodeEnum.ERRORCODE4000);
        }

        Optional<CategoryModel> currentOpt = categoryRepository.findById(request.getId());
        if (currentOpt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Danh mục không tồn tại", StatusCodeEnum.ERRORCODE4000);
        }
        CategoryModel current = currentOpt.get();

        // 1) Chuẩn hoá dữ liệu mới
        String newName = (request.getName() != null) ? request.getName().trim() : current.getName();
        if (newName == null || newName.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Tên danh mục không được trống", StatusCodeEnum.ERRORCODE4000);
        }

        Integer newLevel = (request.getCategoryLevel() != null) ? request.getCategoryLevel() : current.getCategoryLevel();
        if (newLevel == null) {
            return ResponseBuilder.badRequestResponse("Cấp độ danh mục không được trống", StatusCodeEnum.ERRORCODE4000);
        }
        if (newLevel < 1 || newLevel > 3) {
            return ResponseBuilder.badRequestResponse("Cấp độ danh mục không hợp lệ", StatusCodeEnum.ERRORCODE4000);
        }

        Long newParentId = (request.getParentId() != null) ? request.getParentId() :
                (current.getParentCategory() != null ? current.getParentCategory().getId() : null);

        // 2) Validate parent theo level
        CategoryModel newParent = null;

        if (newLevel == 1) {
            if (newParentId != null) {
                return ResponseBuilder.badRequestResponse("Danh mục cấp 1 không được có danh mục cha", StatusCodeEnum.ERRORCODE4000);
            }
        } else {
            // level 2 hoặc 3
            if (newParentId == null) {
                return ResponseBuilder.badRequestResponse("Danh mục cấp 2 trở lên phải có danh mục cha", StatusCodeEnum.ERRORCODE4000);
            }
            if (newParentId.equals(current.getId())) {
                return ResponseBuilder.badRequestResponse("Danh mục không thể là cha của chính nó", StatusCodeEnum.ERRORCODE4000);
            }

            Optional<CategoryModel> parentOpt = categoryRepository.findById(newParentId);
            if (parentOpt.isEmpty()) {
                return ResponseBuilder.badRequestResponse("Danh mục cha không tồn tại", StatusCodeEnum.ERRORCODE4000);
            }
            newParent = parentOpt.get();

            Integer parentLevel = newParent.getCategoryLevel();
            if (parentLevel == null) {
                return ResponseBuilder.badRequestResponse("Danh mục cha không hợp lệ", StatusCodeEnum.ERRORCODE4000);
            }
            if (parentLevel == 3) {
                return ResponseBuilder.badRequestResponse("Không thể tạo danh mục con cho danh mục cấp 3", StatusCodeEnum.ERRORCODE4000);
            }
            if (parentLevel + 1 != newLevel) {
                return ResponseBuilder.badRequestResponse("Cấp độ danh mục không hợp lệ so với danh mục cha", StatusCodeEnum.ERRORCODE4000);
            }

            // 3) Chặn cycle: không cho parent nằm trong cây con của current
            if (isDescendant(current, newParentId)) {
                return ResponseBuilder.badRequestResponse("Danh mục cha không hợp lệ (tạo vòng lặp)", StatusCodeEnum.ERRORCODE4000);
            }
        }

        // 4) Slug + check trùng slug (exclude chính nó)
        String newSlug = Util.generateSlug(newName);
        if (existsBySlugAndLevelExcludeId(newSlug, newLevel, current.getId())) {
            return ResponseBuilder.badRequestResponse("Slug danh mục đã tồn tại", StatusCodeEnum.ERRORCODE4000);
        }

        // 5) Apply update
        current.setName(newName);
        current.setSlug(newSlug);
        current.setCategoryLevel(newLevel);
        current.setParentCategory(newParent);

        if (request.getImageUrl() != null) {
            current.setImageUrl(request.getImageUrl());
        }

        CategoryModel saved = categoryRepository.save(current);
        CmsCreateCategoryResponse response = mapper.map(saved, CmsCreateCategoryResponse.class);

        return ResponseBuilder.okResponse("Cập nhật danh mục thành công", response, StatusCodeEnum.SUCCESS2000);
    }

    /**
     * Check parentId có nằm trong cây con của current không
     * (để tránh current -> ... -> parent trỏ ngược)
     */
    private boolean isDescendant(CategoryModel current, Long candidateParentId) {
        if (candidateParentId == null) return false;
        if (current.getChildCategories() == null) return false;

        for (CategoryModel child : current.getChildCategories()) {
            if (child.getId().equals(candidateParentId)) return true;
            if (isDescendant(child, candidateParentId)) return true;
        }
        return false;
    }

    private boolean existsBySlugAndLevelExcludeId(String slug, Integer level, Long excludeId) {
        return categoryRepository.existsBySlugAndCategoryLevelAndIdNot(slug, level, excludeId);
    }

    @Transactional
    public ResponseEntity<ResponseDTO<Object>> deleteCategory(Long id) {
        if (id == null) {
            return ResponseBuilder.badRequestResponse("Thiếu id danh mục", StatusCodeEnum.ERRORCODE4000);
        }

        Optional<CategoryModel> opt = categoryRepository.findById(id);
        if (opt.isEmpty()) {
            return ResponseBuilder.badRequestResponse("Danh mục không tồn tại", StatusCodeEnum.ERRORCODE4000);
        }
        CategoryModel category = opt.get();

        // 1) Không cho xoá nếu có con
        if (category.getChildCategories() != null && !category.getChildCategories().isEmpty()) {
            return ResponseBuilder.badRequestResponse("Không thể xoá danh mục đang có danh mục con", StatusCodeEnum.ERRORCODE4000);
        }

        // 2) Không cho xoá nếu có sản phẩm
        long productCount = (category.getProductCategories() != null) ? category.getProductCategories().size() : 0;
        if (productCount > 0) {
            return ResponseBuilder.badRequestResponse("Không thể xoá danh mục đang có sản phẩm", StatusCodeEnum.ERRORCODE4000);
        }

        categoryRepository.delete(category);

        return ResponseBuilder.okResponse("Xoá danh mục thành công", null, StatusCodeEnum.SUCCESS2000);
    }

    @Transactional
    public ResponseEntity<ResponseDTO<String>> migrateSlugForAllCategories() {
        try {
            List<CategoryModel> categories = categoryRepository.findAll();
            int updatedCount = 0;

            for (CategoryModel category : categories) {
                if (category.getSlug() == null || category.getSlug().isEmpty()) {
                    String newSlug = Util.generateSlug(category.getName());
                    
                    // Nếu slug bị trùng, thêm suffix id
                    if (existsBySlugAndLevelExcludeId(newSlug, category.getCategoryLevel(), category.getId())) {
                        newSlug = newSlug + "-" + category.getId();
                    }
                    
                    category.setSlug(newSlug);
                    categoryRepository.save(category);
                    updatedCount++;
                }
            }

            return ResponseBuilder.okResponse(
                    "Đã cập nhật slug cho " + updatedCount + " danh mục",
                    "Updated: " + updatedCount,
                    StatusCodeEnum.SUCCESS2000
            );
        } catch (Exception e) {
            log.error("Error migrating slugs: {}", e.getMessage(), e);
            return ResponseBuilder.badRequestResponse("Lỗi khi migrate slug: " + e.getMessage(), StatusCodeEnum.ERRORCODE4000);
        }
    }

}
