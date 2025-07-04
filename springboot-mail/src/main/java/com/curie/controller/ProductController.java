package com.curie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import com.curie.constant.ProductCategory;
import com.curie.dto.ProducrQueryParams;
import com.curie.dto.ProductRequest;
import com.curie.model.Product;
import com.curie.service.ProductService;
import com.curie.util.Page;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Validated // 有用到最大最小值判斷的時候要記得加@Validated
@RestController
public class ProductController {

    @Autowired
    private ProductService productService;


    // 查詢非限定商品->不管有沒有查到都200 OK
    @GetMapping("/products")
    public ResponseEntity<Page<Product>> getProducts(
        // 查詢條件Filtering
        @RequestParam(required = false) ProductCategory category,
        @RequestParam(required = false) String search,
        // 排序 sorting
        @RequestParam(defaultValue = "create_date") String orderBy, //根據何種參數來排列，預設為最新商品在前
        @RequestParam(defaultValue = "desc") String sort,   //決定降冪升冪，預設降冪（大到小）
        // 分頁pagination. (即使資料預設是數字型態，defaultValue值也是要給字串)
        @RequestParam(defaultValue = "5") @Max(1000) @Min(0) Integer limit,  //限制讀取的筆數，避免前端傳很大數字進來導致取太大數字，所以要設定最大值最小值
        @RequestParam(defaultValue = "0") @Min(0) Integer offset //指定略過幾筆資料，並且避免前端回傳負值
    ){
        // 為何讓程式好維護，要把傳入的資料存進一個class中，這樣傳入參數一堆不用數順序，新增參數時也不用每支都改
        ProducrQueryParams producrQueryParams = new ProducrQueryParams();
        // producrQueryParams裡頭的Category存進前端送入的category
        producrQueryParams.setCategory(category);
        producrQueryParams.setSearch(search);
        producrQueryParams.setOrderBy(orderBy);
        producrQueryParams.setSort(sort);
        producrQueryParams.setLimit(limit);
        producrQueryParams.setOffset(offset);

        // 查到的資料以串列形式返回 取得productList
        List<Product> productList =  productService.getProducts(producrQueryParams);

        // 取得 product 總數
        Integer total = productService.countProduct(producrQueryParams);
        
        // 分頁
        Page<Product> page = new Page<>();
        page.setLimit(limit);
        page.setOffset(offset);
        page.setTotal(total);
        page.setResult(productList);

       return ResponseEntity.status(HttpStatus.OK).body(page);
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
