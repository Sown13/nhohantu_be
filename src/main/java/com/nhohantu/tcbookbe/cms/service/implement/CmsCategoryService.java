package com.nhohantu.tcbookbe.cms.service.implement;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateCategoryRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateCategoryResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateProductResponse;
import com.nhohantu.tcbookbe.cms.repository.ICmsCategoryRepository;
import com.nhohantu.tcbookbe.cms.service.ICmsCategoryService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseBuilder;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.common.model.entity.CategoryModel;
import com.nhohantu.tcbookbe.common.model.enums.StatusCodeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CmsCategoryService {
    private final ICmsCategoryService categoryService;
    private final ModelMapper mapper;


    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> createCategory(CmsCreateCategoryRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalArgumentException("Tên danh mục không được trống");
        }

        try {
            CategoryModel parentCategory = null;
            Integer categoryLevel = 0;

            if (request.getParentId() != null) {
                parentCategory = categoryService.findById(request.getParentId())
                        .orElseThrow(() -> new IllegalArgumentException("Danh mục cha không tồn tại"));
                categoryLevel = parentCategory.getCategoryLevel() + 1;
            }
            CategoryModel category = CategoryModel.builder()
                    .name(request.getName())
                    .parentCategory(parentCategory)
                    .categoryLevel(categoryLevel)
                    .build();

            CategoryModel result = categoryService.save(category);
            CmsCreateCategoryResponse response = mapper.map(result, CmsCreateCategoryResponse.class);

            return ResponseBuilder.okResponse("Tạo danh mục thành công", response,
                    StatusCodeEnum.SUCCESS2000);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(e.getMessage(),
                    StatusCodeEnum.ERRORCODE4000);

        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse("Tạo danh mục thất bại. Lỗi khi tạo danh mục",
                    StatusCodeEnum.ERRORCODE4000);
        }

    }
    public ResponseEntity<ResponseDTO<CmsCreateCategoryResponse>> getCategory(Long id){
        try{
            CategoryModel foundCategory = categoryService.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy danh mục với id " + id));

            CmsCreateCategoryResponse response = mapper.map(foundCategory, CmsCreateCategoryResponse.class);
            return ResponseBuilder.okResponse("Lấy thông tin danh mục thành công", response,
                    StatusCodeEnum.SUCCESS2000);
        }catch(IllegalArgumentException e){
            log.error(e.getMessage());
            return ResponseBuilder.badRequestResponse(e.getMessage(),
                    StatusCodeEnum.ERRORCODE4000);
        }
    }
}


