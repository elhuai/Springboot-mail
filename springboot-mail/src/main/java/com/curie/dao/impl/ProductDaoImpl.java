package com.curie.dao.impl;

import com.curie.dao.ProductDao;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;
import com.curie.rowMapper.ProductRowMapper;

import java.util.Date;
import java.util.HashMap;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
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

    public Integer createProduct(ProductRequest productRequest) {
        String sql = "INSERT INTO product (product_name, category, image_url, price, stock, description, create_date, last_modified_date) VALUES (:productName, :category, :imageUrl, :price, :stock, :description,:createDate, :lastModifiedDate)";
        
        // 新創一個map將前端資料都加進去 (key要跟：後面的值一樣)
        Map<String,Object> map = new HashMap<>();
        map.put("productName", productRequest.getProductName());
        // 為何要加.toString()？ // 因為資料庫的類型是String，前端傳過來的類型是Enum，所以要轉換成String
        map.put("category", productRequest.getCategory().toString()); 
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        // 依執行的時間 去創建資料庫
        Date now = new Date();
        map.put("createDate", now);
        map.put("lastModifiedDate", now);

        // 新增一個變數去存資料生成的id 
        // GeneratedKeyHolder? // 它是用來存儲自增主鍵的值，當執行插入操作時，可以獲取到新插入記錄的主鍵值。
        KeyHolder keyHolder = new GeneratedKeyHolder(); // Corrected class name
        //namedParameterJdbcTemplate又是做什麼的？ // 它是用來執行帶有命名參數的SQL語句的，這樣可以避免SQL注入攻擊並提高可讀性。
        namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

        // Integer 可以寫成 int嗎？ // 可以，因為 Integer 是 int 的包裝類型，會自動拆箱。
        // 都要加 .intValue()嗎？ // 是的，因為 keyHolder.getKey() 返回的是 Number 類型，需要轉換成 Integer。
        // keyHolder.getKey() 返回的是 Number 類型，所以需要轉換成 Integer
        Integer productId = keyHolder.getKey().intValue();

        return productId;
    }


}
