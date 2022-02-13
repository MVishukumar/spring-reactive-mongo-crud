package com.demo.service;


import com.demo.dto.ProductDto;
import com.demo.entity.Product;
import com.demo.repository.ProductRepository;
import com.demo.util.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .map(AppUtils::productToDto);
    }

    public Mono<ProductDto> getProduct(String id) {
        return productRepository.findById(id)
                .map(AppUtils::productToDto);
    }

    public Flux<ProductDto> getProductInRange(double min, double max) {
        return productRepository.findByPriceBetween(Range.closed(min, max));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(AppUtils::dtoToProduct)
                .flatMap(productRepository::insert)
                .map(AppUtils::productToDto);

    }

    public Mono<ProductDto> updateProduct(Mono<ProductDto> productDtoMono, String id) {
        return productRepository
                .findById(id)
                .flatMap(product -> productDtoMono.map(AppUtils::dtoToProduct))
                .doOnNext(product -> product.setId(id))
                .flatMap(productRepository::save)
                .map(AppUtils::productToDto);
    }

    public Mono<Void> deleteProduct(String id) {
        return productRepository.deleteById(id);
    }
}
