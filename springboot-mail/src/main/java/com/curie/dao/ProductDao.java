package com.curie.dao;

import java.util.List;

import com.curie.dto.ProducrQueryParams;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;

public interface ProductDao {
    
    List<Product> getProducts(ProducrQueryParams producrQueryParams);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    
    void updateProduct(Integer productId,ProductRequest productRequest);
    void deleteProduct(Integer productId);
    
    
}
