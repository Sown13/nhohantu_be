package com.nhohantu.tcbookbe.business.service;

import com.nhohantu.tcbookbe.common.model.entity.ProductModel;

public interface IProductService {
     ProductModel findById(long id);
     ProductModel addProduct(ProductModel product);
     ProductModel updateProduct(ProductModel product);
     ProductModel deleteProduct(long id);


}
