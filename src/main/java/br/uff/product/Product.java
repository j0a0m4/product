package br.uff.product;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Value
@Builder
@Document(collection = "products")
public class Product {
    @Id String id;
    @NotBlank String name;
    @Positive @NotNull BigDecimal price;
    @NotNull Color color;
}
