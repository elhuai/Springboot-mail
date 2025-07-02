package com.curie.service;

import com.curie.dto.ProductRequest;
import com.curie.model.Product;

public interface ProductService {
    Product getProductById(Integer productId);

    // createProduct方法去接收前端的參數，但執行後會回傳proudctId->數字型態
    Integer createProduct(ProductRequest productRequest);

    // 因為 無返回值 所以寫 void
    void updateProduct(Integer productId,ProductRequest productRequest);
}
