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
    public String createProduct(final Product product) {
        return productsRepository.save(product).getId();
    }

    @Override
    public List<Product> getAllProducts() {
        return productsRepository.findAll();
    }

    @Override
    public Product getProductById(final String id) {
        return productsRepository.findById(id).orElseThrow();
    }
}
