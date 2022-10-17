package br.uff.product;

import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class Product {
    UUID id;
    @NotBlank String name;
    @Positive @NotNull BigDecimal price;
    @NotNull Color color;
}
