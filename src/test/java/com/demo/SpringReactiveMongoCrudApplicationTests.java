package com.demo;

import com.demo.controller.ProductController;
import com.demo.dto.ProductDto;
import com.demo.service.ProductService;
import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(ProductController.class)
class SpringReactiveMongoCrudApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private ProductService productService;

	private ProductDto PRODUCT_TV;
	private ProductDto PRODUCT_TABLE;

	@BeforeEach
	public void initProduct() {
		PRODUCT_TV = new ProductDto("1", "Tv", 1, 18000);
		PRODUCT_TABLE = new ProductDto("2", "Table", 1, 5000);
	}

	@Test
	public void addNewProductTest() {
		Mono<ProductDto> newProduct = Mono.just(PRODUCT_TV);
		when(productService.saveProduct(newProduct)).thenReturn(newProduct);

		webTestClient
				.post()
				.uri("/products")
				.body(Mono.just(newProduct), ProductDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void getAllProductsTest() {
		Flux<ProductDto> products = Flux.just(PRODUCT_TV, PRODUCT_TABLE);
		when(productService.getAllProducts()).thenReturn(products);

		Flux<ProductDto> responseBody =
				webTestClient
						.get()
						.uri("/products")
						.exchange()
						.expectStatus().isOk()
						.returnResult(ProductDto.class)
						.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(PRODUCT_TV)
				.expectNext(PRODUCT_TABLE)
				.verifyComplete();
	}

	@Test
	public void getProductByIdTest() {
		Mono<ProductDto> productTv = Mono.just(PRODUCT_TV);
		when(productService.getProduct(anyString())).thenReturn(productTv);

		Flux<ProductDto> responseBody =
				webTestClient
						.get()
						.uri("/products/1")
						.exchange()
						.expectStatus().isOk()
						.returnResult(ProductDto.class)
						.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(productDto -> productDto.getName().equals(PRODUCT_TV.getName()))
				.verifyComplete();
	}

	@Test
	public void updateProductTest() {
		Mono<ProductDto> productTv = Mono.just(PRODUCT_TV);
		when(productService.updateProduct(productTv, "1")).thenReturn(productTv);

		webTestClient
						.put()
						.uri("/products/update/1")
						.body(Mono.just(productTv), ProductDto.class)
						.exchange()
						.expectStatus().isOk();
	}

	@Test
	public void deleteProductTest() {
		given(productService.deleteProduct(anyString())).willReturn(Mono.empty());

		webTestClient
				.delete()
				.uri("/products/delete/1")
				.exchange()
				.expectStatus().isOk();
	}
}
