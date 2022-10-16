package br.uff.product;

import java.math.BigDecimal;
import java.util.UUID;

public record Product(UUID id, String name, BigDecimal price, Color color) {
}
