package br.uff.product;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.UUID;

public record Product(UUID id,
                      @NotBlank String name,
                      @Positive BigDecimal price,
                      @NotNull Color color) {
}
