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
    public ResponseEntity<Object> createProduct(@RequestBody @Valid final Product product,
                                                final UriComponentsBuilder uriBuilder) {
        final var id = productUseCases.create(product);
        final var location = uriBuilder.path("/v1/products/{id}").build(id);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        final var products = productUseCases.getAll();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable("id") final String id) {
        final var product = productUseCases.getById(id);
        return ResponseEntity.ok(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteById(@PathVariable("id") final String id) {
        productUseCases.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateById(@PathVariable("id") final String id,
                                              @RequestBody @Valid final Product product) {
         productUseCases.updateById(id, product);
        return ResponseEntity.noContent().build();
    }
}
