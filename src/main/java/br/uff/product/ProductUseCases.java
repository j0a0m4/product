package br.uff.product;


import java.util.List;

public interface ProductUseCases {
    String create(Product product);
    List<Product> getAll();

    Product getById(String id);

    void deleteById(String id);

    void updateById(String id, Product product);
}
