package br.uff.product;


import java.util.List;

public interface ProductUseCases {
    String createProduct(final Product product);
    List<Product> getProducts();
}
