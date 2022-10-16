package br.uff.product;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductsService implements ProductsUseCases {
    @Override
    public UUID createProduct(Product product) {
        return null;
    }
}
