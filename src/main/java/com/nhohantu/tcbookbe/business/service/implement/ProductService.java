package com.nhohantu.tcbookbe.business.service.implement;

import com.nhohantu.tcbookbe.business.repository.IProductRepository;
import com.nhohantu.tcbookbe.business.service.IProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {
    private IProductRepository productRepository;
}
