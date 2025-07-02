package com.curie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.curie.dto.ProductRequest;
import com.curie.model.Product;
import com.curie.service.ProductService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Integer productId) {
        Product product = productService.getProductById(productId);

        if (product != null) {
            return ResponseEntity.status(HttpStatus.OK).body(product);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/createProduct") // 有設定 NotNull 就要記得放 Valid
    public  ResponseEntity<Product> createProduct(@RequestBody @Valid ProductRequest productRequest) {
        // 預期ProductService會有一個createProduct的方法，會傳進一個參數productRequest，執行完畢後會回傳一個productId
        Integer productId = productService.createProduct(productRequest);

        // 新增商品成功後，可以利用productId查詢在資料庫的商品數據回來
        Product product = productService.getProductById(productId);
        
        // 回傳給前端 201CREATE 然後把查到的數據回傳給前端
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    // 更新商品
    @PutMapping("/updateProduct/{productId}") //  只有這個變數 ProductRequest 允許前端去修改的，並且可以限定前端不會改到其他資料
    public ResponseEntity<Product> updateProduct(@PathVariable Integer productId,@RequestBody @Valid ProductRequest productRequest) {
        
        // 先檢查有沒有這商品
        Product product = productService.getProductById(productId);
        if (product == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        // 商品若存在就去更新商品
        // 無返回值 所以不用特別存變數
        productService.updateProduct(productId,productRequest);

        // 格式是Product 利用id
        Product updateProduct = productService.getProductById(productId);

        // 回傳給前端 201CREATE 然後把查到的數據回傳給前端
        return ResponseEntity.status(HttpStatus.OK).body(updateProduct);
    }

}
