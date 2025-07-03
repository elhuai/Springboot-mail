package com.curie.service.impl;
import com.curie.constant.ProductCategory;
import com.curie.dao.ProductDao;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;
import com.curie.service.ProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;

    

    @Override
    public List<Product> getProducts(ProducrQueryParams producrQueryParams) {
        return productDao.getProducts(producrQueryParams);
    }

    @Override
    public Product getProductById(Integer productId) {
        return productDao.getProductById(productId);
    }

    // createProdct方法實作
    @Override
    public Integer createProduct(ProductRequest productRequest) {
        // service 層去call Dao 的 createProduct 並且將前端參數傳入 -> 執行資料庫
        return productDao.createProduct(productRequest);
    }

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        productDao.updateProduct(productId,productRequest);
        
    }

    @Override
    public void deleteProduct(Integer productId) {
        productDao.deleteProduct(productId);
        
    }

    
    

}
