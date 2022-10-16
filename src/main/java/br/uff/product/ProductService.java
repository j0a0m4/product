package br.uff.product;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductService implements ProductUseCases {
    @Override
    public UUID createProduct(Product product) {
        return null;
    }
}
