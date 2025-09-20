package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.response.GetProductDetailResponse;
import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.business.service.ProductService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getProducts(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return productService.getProducts(pageNumber, pageSize, sortBy, sortDirection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO<GetProductDetailResponse>> getProductDetail(@PathVariable("id") Long id) {
        return productService.getProductDetailById(id);
    }
}