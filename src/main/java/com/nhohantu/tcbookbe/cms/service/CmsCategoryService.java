package com.nhohantu.tcbookbe.cms.service;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsListCategoryResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsCategoryRepository;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.model.enums.CategoryLevelDefault;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CmsCategoryService {
    private final ICmsCategoryRepository categoryService;
    private final ModelMapper mapper;

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
                Optional<CategoryModel> parentOptional = categoryService.findById(request.getParentId());
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

            CategoryModel category = CategoryModel.builder().name(request.getName()).parentCategory(parentCategory).categoryLevel(categoryLevel).build();

            CategoryModel result = categoryService.save(category);
            CmsCreateCategoryResponse response = mapper.map(result, CmsCreateCategoryResponse.class);

            return ResponseBuilder.okResponse("Tạo danh mục thành công", response, StatusCodeEnum.SUCCESS2000);

        } catch (IllegalArgumentException e) {
            return ResponseBuilder.badRequestResponse(e.getMessage(), StatusCodeEnum.ERRORCODE4000);
        } catch (Exception e) {
            log.error("Lỗi khi tạo danh mục: " + e.getMessage(), e);
            return ResponseBuilder.badRequestResponse("Tạo danh mục thất bại. Lỗi khi tạo danh mục", StatusCodeEnum.ERRORCODE4000);
        }
    }

    //    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> getCategory(Long id) {
//        try {
//            CategoryModel foundCategory = categoryService.findById(id)
//                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với id " + id));
//
//            CmsCreateCategoryResponse response = mapper.map(foundCategory, CmsCreateCategoryResponse.class);
//            return ResponseBuilder.okResponse("Lấy thông tin danh mục thành công", response,
//                    StatusCodeEnum.SUCCESS2000);
//        } catch (IllegalArgumentException e) {
//            log.error(e.getMessage());
//            return ResponseBuilder.badRequestResponse(e.getMessage(),
//                    StatusCodeEnum.ERRORCODE4000);
//        }
//    }
    public ResponseEntity<ResponseDTO<List<CmsCreateCategoryResponse>>> findAllCategoryLevel3() {
        try {
            List<CategoryModel> categories = categoryService.findByCategoryLevel(3);

            List<CmsCreateCategoryResponse> responseList = categories.stream().map(category -> mapper.map(category, CmsCreateCategoryResponse.class)).collect(Collectors.toList());

            return ResponseBuilder.okResponse("Lấy danh sách danh mục cấp 3 thành công", responseList, StatusCodeEnum.SUCCESS2000);

        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(e.getMessage(), StatusCodeEnum.ERRORCODE4000);
        }

    }

    public ResponseEntity<ResponseDTO<List<CmsListCategoryResponse>>> findAllCategory() {
        try {
            List<CategoryModel> allCategories = categoryService.findAll();

            //convert CategoryModel -> CmsListCategoryResponse
            Map<Long, CmsListCategoryResponse> categoryMap = allCategories.stream()
                    .map(category -> mapper.map(category, CmsListCategoryResponse.class))
                    .collect(Collectors.toMap(CmsListCategoryResponse::getId, category -> category));
            // Build category tree
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
                    rootCategories.add(category); //add categories lv1 in list
                }
            }
            return ResponseBuilder.okResponse(
                    "Lấy danh sách danh mục thành công",
                    rootCategories,
                    StatusCodeEnum.SUCCESS2000
            );
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(
                    e.getMessage(),
                    StatusCodeEnum.ERRORCODE4000
            );
        }
    }

}


