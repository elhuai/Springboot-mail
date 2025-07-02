package com.curie.dao;

import com.curie.dto.ProductRequest;
import com.curie.model.Product;

public interface ProductDao {
    
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    
    void updateProduct(Integer productId,ProductRequest productRequest);
    void deleteProduct(Integer productId);
    
    
}
