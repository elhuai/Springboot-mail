package com.curie.dao;

import com.curie.model.Product;

public interface ProductDao {
    
    Product getProductById(Integer productId);
    
}
