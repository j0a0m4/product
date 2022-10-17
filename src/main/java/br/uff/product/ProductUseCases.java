package br.uff.product;


import java.util.List;

public interface ProductUseCases {
    String createProduct(Product product);
    List<Product> getAllProducts();

    Product getProductById(String id);
}
