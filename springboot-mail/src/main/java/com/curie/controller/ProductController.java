package com.curie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.curie.constant.ProductCategory;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;
import com.curie.service.ProductService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    // 查詢非限定商品->不管有沒有查到都200 OK
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProducts(
        @RequestParam(required = false) ProductCategory category,
        @RequestParam(required = false) String search ){
        // 查到的資料以串列形式返回
       List<Product> productList =  productService.getProducts(category,search);

       return ResponseEntity.status(HttpStatus.OK).body(productList);
    }

    // 查詢單一商品 找不到要NOT_FOUND 但只要找到要200 OK
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

    // 刪除商品
    // 為什麼是<?>? // 因為這個方法不需要返回任何內容，所以可以使用通配符<?>來表示返回類型不確定。
    @DeleteMapping("/deleteProduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Integer productId) {
        // 不做判定商品是否存在再刪除 因為怎麼樣都是要將商品刪除，故不用特別做資料判定
         productService.deleteProduct(productId);

        // 刪除要是  HttpStatus.NO_CONTENT
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




}
