package com.curie.dto;

import com.curie.constant.ProductCategory;

public class ProducrQueryParams {
    ProductCategory category;
    String search;
    String orderBy;
    String sort;



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
    public String getOrderBy() {
        return orderBy;
    }
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }
    public String getSort() {
        return sort;
    }
    public void setSort(String sort) {
        this.sort = sort;
    }
    
    
}
