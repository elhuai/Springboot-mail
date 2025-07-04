package com.curie.service;

import java.util.List;

import com.curie.constant.ProductCategory;
import com.curie.dto.ProducrQueryParams;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;

public interface ProductService {
    // 返回值資料型態 Controller執行名稱 （）中放入有沒有前端資料要傳進後端的參數
    List<Product> getProducts(ProducrQueryParams producrQueryParams);
    Integer countProduct(ProducrQueryParams producrQueryParams);

    Product getProductById(Integer productId);

    // createProduct方法去接收前端的參數，但執行後會回傳proudctId->數字型態
    Integer createProduct(ProductRequest productRequest);

    // 因為 無返回值 所以寫 void
    void updateProduct(Integer productId,ProductRequest productRequest);
    void deleteProduct(Integer productId);
}
