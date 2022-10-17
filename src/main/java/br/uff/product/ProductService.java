package br.uff.product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements ProductUseCases {

    final ProductsRepository productsRepository;

    public ProductService(final ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public String create(final Product product) {
        return productsRepository.save(product).getId();
    }

    @Override
    public List<Product> getAll() {
        return productsRepository.findAll();
    }

    @Override
    public Product getById(final String id) {
        return productsRepository.findById(id).orElseThrow();
    }

    @Override
    public void deleteById(String id) {
        productsRepository.findById(id).orElseThrow();
        productsRepository.deleteById(id);
    }

    @Override
    public void updateById(String id, Product product) {
        productsRepository.findById(id).orElseThrow();
        final var updatedProduct = Product.builder()
                .id(id)
                .name(product.getName())
                .price(product.getPrice())
                .color(product.getColor())
                .build();
        productsRepository.save(updatedProduct);
    }
}
