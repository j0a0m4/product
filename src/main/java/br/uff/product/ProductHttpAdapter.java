package br.uff.product;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

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
}
