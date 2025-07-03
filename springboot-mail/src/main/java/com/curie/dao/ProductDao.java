package com.curie.dao;

import java.util.List;

import com.curie.constant.ProductCategory;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;

public interface ProductDao {
    
    List<Product> getProducts(ProductCategory category, String search);
    Product getProductById(Integer productId);
    Integer createProduct(ProductRequest productRequest);
    
    void updateProduct(Integer productId,ProductRequest productRequest);
    void deleteProduct(Integer productId);
    
    
}
