package com.nhohantu.tcbookbe.business.service.implement;

import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.business.service.IProductService;
import com.nhohantu.tcbookbe.common.model.entity.ProductModel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {
    private IProductRepository productRepository;

    @Override
    public ProductModel findById(long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public ProductModel addProduct(ProductModel product) {
        return productRepository.save(product);
    }

    @Override
    public ProductModel updateProduct(ProductModel product) {
        ProductModel updateProductModel = productRepository.findById(product.getId()).orElse(null);
        if(updateProductModel != null ){
            return productRepository.save(updateProductModel);
        }
        return null;
    }

    @Override
    public ProductModel deleteProduct(long id) {
         ProductModel deleteProductModel = productRepository.findById(id).orElse(null);
         if(deleteProductModel != null){
              productRepository.deleteById(id);
              return deleteProductModel;
         }
         return null;
    }

}
