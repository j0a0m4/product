package br.uff.product;

import java.util.UUID;

public interface ProductsUseCases {
    UUID createProduct(final Product product);
}
