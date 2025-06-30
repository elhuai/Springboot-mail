package com.curie.dao.impl;

import com.curie.dao.ProductDao;
import com.curie.model.Product;
import com.curie.rowMapper.ProductRowMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

// 執行資料庫的邏輯

@Component
public class ProductDaoImpl implements ProductDao  {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id,product_name, category, image_url, price, stock, description, create_date,"
                    +"last_modified_date FROM product WHERE product_id =:productId";
        Map<String,Object> map = new HashMap<>();
        map.put("productId", productId);


        List<Product> productList =namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper() );
        if (productList.size()>0) {
            return productList.get(0);
        }else{
            return null;
        }
        // return null;
    }
    

}
