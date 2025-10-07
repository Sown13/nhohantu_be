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

import java.util.List;

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

//    @PutMapping
//    public ResponseEntity<ResponseDTO<CmsCreateProductResponse>> updateProduct(@RequestBody CmsCreateProductRequest request) {
//        return null;
//    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable Long id) {
//        return ResponseEntity.ok(productService.getProduct(id));
//    }
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
    @PutMapping("/{id}")
    public ResponseEntity<ResponseDTO<CmsCreateProductResponse>> updateProduct(
            @PathVariable Long id,
            @RequestBody CmsCreateProductRequest request) {

        CmsCreateProductResponse updatedProduct = productService.updateProduct(id, request);

        ResponseDTO<CmsCreateProductResponse> response = ResponseDTO.<CmsCreateProductResponse>builder()
                .success(true)
                .message("Product updated successfully")
                .data(updatedProduct)
                .build();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        ResponseDTO<Void> response = ResponseDTO.<Void>builder()
                .success(true)
                .message("Product deleted successfully")
                .build();

        return ResponseEntity.ok(response);
    }

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
