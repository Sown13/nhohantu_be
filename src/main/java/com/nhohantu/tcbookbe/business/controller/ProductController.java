package com.nhohantu.tcbookbe.business.controller;

import com.nhohantu.tcbookbe.business.repository.ProductRepository;
import com.nhohantu.tcbookbe.business.service.implement.ProductService;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

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
