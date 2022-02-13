package com.demo.controller;

import com.demo.dto.ProductDto;
import com.demo.entity.Product;
import com.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping({"", "/"})
    public Flux<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{productId}")
    public Mono<ProductDto> getProductById(@PathVariable("productId") String id) {
        return productService.getProduct(id);
    }

    @GetMapping("/product-range")
    public Flux<ProductDto> getProductById(@RequestParam("min") double min,
                                           @RequestParam("max") double max) {
        return productService.getProductInRange(min, max);
    }

    @PostMapping({"", "/"})
    public Mono<ProductDto> saveProduct(@RequestBody Mono<ProductDto> productDtoMono) {
        return productService.saveProduct(productDtoMono);
    }

    @PutMapping("/update/{productId}")
    public Mono<ProductDto> updateProduct(@RequestBody Mono<ProductDto> productDtoMono,
                                          @PathVariable("productId") String id) {
        return productService.updateProduct(productDtoMono, id);
    }

    @DeleteMapping("/delete/{productId}")
    public Mono<Void> deleteProduct(@PathVariable("productId") String id) {
        return productService.deleteProduct(id);
    }

}
