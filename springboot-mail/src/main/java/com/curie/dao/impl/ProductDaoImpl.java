package com.curie.dao.impl;

import com.curie.constant.ProductCategory;
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
    public List<Product> getProducts(ProductCategory category, String search) {
        String sql ="SELECT product_id,product_name, category, image_url, price, stock, description, create_date, last_modified_date FROM product "+
        "WHERE 1=1"; //利用WHERE 1=1 讓整個sql判斷可以讓後面任意加上多組篩選
        
        // 就算沒有傳入值也要加這行 去將資料轉為想要的型態
        Map<String,Object> map = new HashMap<>();
        if (category != null){
            sql = sql + " AND category=:category"; // 記得要預留一個空白鍵 這樣接上sql語句才不會報錯
            map.put("category", category.name());  //category是Enum方法，所以要用name()把它轉為字串
        }
        if (search != null){
            sql = sql + " AND (product_name LIKE :search OR description LIKE :search)"; // 記得不可以寫成%:search% 只能寫在map.put()裡面
            map.put("search", "%" + search + "%");  //因為只是需要模糊符合所以加上％：%XX%只要裡面有XX即可；XX% XX開頭；%XX XX結尾的資料
        }

        // 利用ProductRowMapper整理每個查好的資料，然後存入
        List<Product> productList =namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        
        return productList;
    }

    @Override
    public Product getProductById(Integer productId) {
        String sql = "SELECT product_id,product_name, category, image_url, price, stock, description, create_date,"
                    +"last_modified_date FROM product WHERE product_id =:productId";
        
        Map<String,Object> map = new HashMap<>();
        map.put("productId", productId);


        List<Product> productList =namedParameterJdbcTemplate.query(sql, map, new ProductRowMapper());
        if (productList.size()>0) {
            return productList.get(0);
        }else{
            return null;
        }
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

    @Override
    public void updateProduct(Integer productId, ProductRequest productRequest) {
        String sql = "UPDATE product SET product_name = :productName, category = :category, image_url = :imageUrl, price = :price, stock = :stock, description = :description,  last_modified_date = :lastModifiedDate WHERE product_id = :productId";
        
        Map<String,Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("productName",productRequest.getProductName());
        map.put("category", productRequest.getCategory().toString());
        map.put("imageUrl", productRequest.getImageUrl());
        map.put("price", productRequest.getPrice());
        map.put("stock", productRequest.getStock());
        map.put("description", productRequest.getDescription());

        map.put("lastModifiedDate",  new Date());

        namedParameterJdbcTemplate.update(sql, map);

    }


    // 刪除商品
    @Override
    public void deleteProduct(Integer productId) {
        String sql = "DELETE FROM product WHERE product_id = :productId";

        Map<String,Object> map = new HashMap<>();
        map.put("productId",productId);

        namedParameterJdbcTemplate.update(sql, map);
    }
}
