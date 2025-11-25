package com.nhohantu.tcbookbe.cms.controller;

import com.nhohantu.tcbookbe.cms.dto.request.CmsCreateProductRequest;
import com.nhohantu.tcbookbe.cms.dto.response.CmsCreateProductResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsGetProductDetailResponse;
import com.nhohantu.tcbookbe.cms.dto.response.CmsProductPageResponse;
import com.nhohantu.tcbookbe.cms.service.CmsGetProductDetailService;
import com.nhohantu.tcbookbe.cms.service.CmsProductService;

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

    @PostMapping
    public ResponseEntity<ResponseDTO<CmsCreateProductResponse>> createProduct(@RequestBody CmsCreateProductRequest request) {
        return productService.createProduct(request);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.getProduct(id));
//    }
@GetMapping
public ResponseEntity<ResponseDTO<CmsProductPageResponse>> getProductsPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
) {
    // Gọi service để lấy dữ liệu phân trang
    CmsProductPageResponse paginatedProducts = productService.getProductsPaginated(page, size);

    ResponseDTO<CmsProductPageResponse> response = ResponseDTO.<CmsProductPageResponse>builder()
            .success(true)
            .message("Products fetched successfully")
            .data(paginatedProducts)
            .build();

    return ResponseEntity.ok(response);
}

@GetMapping("/get/all")
public ResponseEntity<ResponseDTO<List<CmsGetProductDetailResponse>>> getAllProducts() {
    List<CmsGetProductDetailResponse> products = getproductDetailService.getAllProducts();

    ResponseDTO<List<CmsGetProductDetailResponse>> response = ResponseDTO.<List<CmsGetProductDetailResponse>>builder()
            .success(true)
            .message("Get all products successfully")
            .data(products)
            .build();

    return ResponseEntity.ok(response);
}

    // TODO: update, delete
}
