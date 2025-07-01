package com.curie.constant;

public class EnumDescription {
    // 利用 name() 方法將 Enum 的值轉換成 string 類型
    public static void main(String[] args){
        ProductCategory category = ProductCategory.FOOD;
        String s = category.name();
        System.out.println(s); //FOOD

    // 利用valueOf(s2)根據字串去搜尋對應到的是哪個Enum固定值
        String s2 = "CAR";
        ProductCategory category2 = ProductCategory.valueOf(s2);
        System.out.println(category2); //CAR
        // 若有存在就會將找到的 "CAR" 存放在category2 裡面
    }

   
}
