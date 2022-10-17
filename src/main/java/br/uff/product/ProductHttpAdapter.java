package br.uff.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductHttpAdapter {
    final ProductUseCases productUseCases;

    public ProductHttpAdapter(final ProductUseCases productUseCases) {
        this.productUseCases = productUseCases;
    }

    @PostMapping
    public ResponseEntity<Object> createProduct(final @RequestBody @Valid Product product,
                                                final UriComponentsBuilder uriBuilder) {
        final var id = productUseCases.createProduct(product);
        final var location = uriBuilder.path("/v1/products/{id}").build(id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAllProducts() {
        final var products = productUseCases.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findOneProduct(@PathVariable("id") final String id) {
        final var product = productUseCases.getProductById(id);
        return ResponseEntity.ok(product);
    }
}
