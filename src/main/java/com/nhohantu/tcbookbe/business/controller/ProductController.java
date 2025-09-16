package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.dto.response.GetProductDetailResponse;
import com.nhohantu.tcbookbe.business.dto.response.GetProductListResponse;
import com.nhohantu.tcbookbe.business.service.ProductService;
import com.nhohantu.tcbookbe.common.model.builder.ResponseDTO;
import com.nhohantu.tcbookbe.business.repository.ProductRepository;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.*;

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

    private final ProductService productService;
    @GetMapping("/{id}")
    public ProductModel getProductById(@PathVariable Long id){
        return productService.findById(id);
    }

    @PostMapping ("/add")
    public ProductModel addProduct(@RequestBody ProductModel product){
        return productService.addProduct(product);
    }

    @PutMapping("/update")
    public ProductModel updateProduct(@RequestBody ProductModel product){
        return productService.updateProduct(product);
    }

    @DeleteMapping("/delete")
    public ProductModel deleteProduct(@RequestParam long id){
        return productService.deleteProduct(id);
    }
}
