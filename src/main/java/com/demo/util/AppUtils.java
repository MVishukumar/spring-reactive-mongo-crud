package com.demo.util;

import com.demo.dto.ProductDto;
import com.demo.entity.Product;
import org.springframework.beans.BeanUtils;

public class AppUtils {

    public static ProductDto productToDto(Product product) {
        ProductDto productDto = new ProductDto();
        BeanUtils.copyProperties(product, productDto);

        return productDto;
    }


    public static Product dtoToProduct(ProductDto productDto) {
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);

        return product;
    }


}
