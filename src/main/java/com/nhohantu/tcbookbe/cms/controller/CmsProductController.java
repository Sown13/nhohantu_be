package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateProductRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateProductResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsGetProductDetailResponse;
import com.nhohantu.tcbookbe.cms.service.CmsGetProductDetailService;
import com.nhohantu.tcbookbe.cms.service.CmsProductService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/products")
@RequiredArgsConstructor
public class CmsProductController {

    private final CmsProductService productService;
    private final CmsGetProductDetailService getproductDetailService;

    @PostMapping
    public ResponseEntity<ResponseDTO<CmsCreateProductResponse>> createProduct(@RequestBody CmsCreateProductRequest request) {
        return productService.createProduct(request);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.getProduct(id));
//    }

    // TODO: update, delete

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDTO<CmsGetProductDetailResponse>> getProductDetail(@PathVariable Long id) {
        CmsGetProductDetailResponse response = getproductDetailService.getProductDetailById(id);

        ResponseDTO<CmsGetProductDetailResponse> responseDTO = ResponseDTO.<CmsGetProductDetailResponse>builder()
                .success(true)
                .message("Success")
                .data(response)
                .build();

        return ResponseEntity.ok(responseDTO);
    }

}
