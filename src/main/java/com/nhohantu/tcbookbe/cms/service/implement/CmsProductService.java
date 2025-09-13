package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateProductRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateProductResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsCategoryRepository;
import com.nhohantu.tcbookbe.cms.repository.ICmsProductCategoryRepository;
import com.nhohantu.tcbookbe.cms.service.ICmsProductService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductCategoryModel;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import com.nhohantu.tcbookbe.common.service.UserBasicInfoService;
import com.nhohantu.tcbookbe.common.utils.Constant;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CmsProductService {
    private final UserBasicInfoService userBasicInfoService;
    private final ModelMapper mapper;
    private final ICmsProductService productRepository;
    private final ICmsCategoryRepository categoryRepository;
    private final ICmsProductCategoryRepository productCategoryRepository;

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

        //check category định gán //todo chuyển qua tạo validate anotation
        List<CategoryModel> categories = categoryRepository.findAllById(request.getCategoryIds());
        if (categories.isEmpty()) {
            throw new IllegalArgumentException("Category không hợp lệ");
        }

        ProductModel product = ProductModel.builder()
                .name(request.getName())
                .desciption(request.getDesciption())
                .price(request.getPrice())
                .quantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .active(request.getActive() != null ? request.getActive() : false)
                .mainImageUrl(request.getMainImageUrl() != null ? request.getMainImageUrl() : Constant.DEFAULT_IMAGE_URL)
                .build();

        List<ProductCategoryModel> productCategories = new ArrayList<>();
        for (CategoryModel cat : categories) {
            ProductCategoryModel productCategory = ProductCategoryModel.builder()
                    .product(product)
                    .category(cat)
                    .build();
            productCategories.add(productCategory);
        }

        product.setProductCategories(productCategories);

        try {
            ProductModel result = productRepository.save(product);
            CmsCreateProductResponse response = mapper.map(result, CmsCreateProductResponse.class);

            return ResponseBuilder.okResponse("Tạo sản phẩm thành công", response, StatusCodeEnum.SUCCESS2000);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse("Tạo sản phẩm thất bại. Lỗi khi tạo sản phẩm",
                    StatusCodeEnum.ERRORCODE4000);
        }
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
