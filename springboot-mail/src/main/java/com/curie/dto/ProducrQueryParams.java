package com.curie.dto;

import com.curie.constant.ProductCategory;

public class ProducrQueryParams {
    ProductCategory category;
    String search;
    
    public ProductCategory getCategory() {
        return category;
    }
    public void setCategory(ProductCategory category) {
        this.category = category;
    }
    public String getSearch() {
        return search;
    }
    public void setSearch(String search) {
        this.search = search;
    }

    
}
