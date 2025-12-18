package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.response.GetProductDetailResponse;
import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.business.dto.response.ProductDetailResponse;
import com.nhohantu.tcbookbe.business.service.ProductService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
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

    @GetMapping("/{slug}")
    public ResponseEntity<ResponseDTO<ProductDetailResponse>> getProductDetailSlug(@PathVariable String slug) {
        return productService.getProductBySlug(slug);
    }

    @GetMapping("/search")
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> searchProducts(
            @RequestParam(value = "text", required = false) String text,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "active", required = false) Boolean active,
            @RequestParam(value = "price_min", required = false) BigDecimal priceMin,
            @RequestParam(value = "price_max", required = false) BigDecimal priceMax,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "limit", defaultValue = "10") Integer limit,
            @RequestParam(value = "sort_by", defaultValue = "id_asc") String sortBy
    ) {
        return productService.searchProducts(
                text, category, active, priceMin, priceMax, page, limit, sortBy
        );
    }

    @GetMapping("/best-seller-products")
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getBestSellerProducts(
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return productService.getBestSellerProducts(limit);
    }

    @GetMapping("/on-sale-products")
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getOnSaleProducts(
            @RequestParam(defaultValue = "10") Integer limit
    ) {
        return productService.getMostOrderedProducts(limit);
    }

    @GetMapping("/{slug}/related")
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> getRelatedProducts(@PathVariable String slug) {
        return productService.getRelatedProducts(slug);
    }

    @GetMapping("/search-products")
    public ResponseEntity<ResponseDTO<List<GetProductListResponse>>> searchProducts(
            @RequestParam(name = "text") String text
    ) {
        // Simply delegate to the service
        return productService.clientSearchProducts(text);
    }
}