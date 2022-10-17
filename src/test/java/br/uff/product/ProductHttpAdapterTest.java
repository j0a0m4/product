package br.uff.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductHttpAdapterTest {

    private final String API_ENDPOINT = "/v1/products";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    ProductHttpAdapter productHttpAdapter;

    @MockBean
    ProductUseCases productUseCases;

    @Nested
    class CreateProduct {
        @Test
        @DisplayName("POST /v1/products - SUCCESS")
        void shouldCreateProductSuccessfully() throws Exception {
            final var product = new Product(null, "Camisa Teste", BigDecimal.valueOf(89.90), Color.BRANCO);
            final var requestBody = objectMapper.writeValueAsString(product);
            final var id = UUID.randomUUID().toString();

            doReturn(id)
                    .when(productUseCases)
                    .create(any());

            mockMvc.perform(post(API_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString(id)));
        }

        @Test
        @DisplayName("POST /v1/products - BAD REQUEST")
        void shouldReturnBadRequestWhenValidationFails() throws Exception {
            final var products = List.of(
                    new Product(null, "", BigDecimal.valueOf(89.90), Color.BRANCO),
                    new Product(null, "  ", BigDecimal.valueOf(89.90), Color.BRANCO),
                    new Product(null, "Camisa Teste", BigDecimal.valueOf(89.90), null),
                    new Product(null, "Camisa Teste", null, Color.BRANCO),
                    new Product(null, "Camisa Teste", BigDecimal.ZERO, Color.BRANCO)
            );

            for (Product product : products) {
                testBadRequest(product);
            }
        }

        @Test
        @DisplayName("POST /v1/products - INTERNAL SERVER ERROR")
        void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
            final var product = new Product(null, "Camisa Teste", BigDecimal.valueOf(89.90), Color.BRANCO);
            final var requestBody = objectMapper.writeValueAsString(product);

            doThrow(new RuntimeException("Test Exception"))
                    .when(productUseCases)
                    .create(any());

            mockMvc.perform(post(API_ENDPOINT)
                            .contentType(APPLICATION_JSON)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }

        private void testBadRequest(final Product product) throws Exception {
            final var requestBody = objectMapper.writeValueAsString(product);

            mockMvc.perform(post(API_ENDPOINT)
                            .contentType(APPLICATION_JSON_VALUE)
                            .content(requestBody))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    class ReadAllProducts {
        @Test
        @DisplayName("GET /v1/products - SUCCESS")
        void shouldReadAllProductSuccessfully() throws Exception {
            final var productsMock = List.of(
                    new Product(null, "Camisa Teste", BigDecimal.valueOf(89.90), Color.BRANCO),
                    new Product(null, "Jaqueta Jeans", BigDecimal.valueOf(349.00), Color.AZUL)
            );

            doReturn(productsMock)
                    .when(productUseCases)
                    .getAll();

            mockMvc.perform(get(API_ENDPOINT)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(content().contentType(APPLICATION_JSON))
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /v1/products - INTERNAL SERVER ERROR")
        void shouldReturnInternalServerErrorWhenServiceThrowsException() throws Exception {
            doThrow(new RuntimeException("Test Exception"))
                    .when(productUseCases)
                    .getAll();

            mockMvc.perform(get(API_ENDPOINT)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isInternalServerError());
        }
    }

    @Nested
    class ReadProduct {
        @Test
        @DisplayName("GET /v1/products/{id} - SUCCESS")
        void shouldReadOneProductSuccessfully() throws Exception {
            final var id = UUID.randomUUID().toString();
            final var product = new Product(id, "Camisa Teste", BigDecimal.valueOf(89.90), Color.BRANCO);


            doReturn(product)
                    .when(productUseCases)
                    .getById(id);

            mockMvc.perform(get(API_ENDPOINT + "/" + id)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(jsonPath("$").isNotEmpty())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("GET /v1/products/{id} - NOT FOUND")
        void shouldReturnNotFoundWhenProductIsNotFound() throws Exception {
            final var id = UUID.randomUUID().toString();

            doThrow(new NoSuchElementException())
                    .when(productUseCases)
                    .getById(id);

            mockMvc.perform(get(API_ENDPOINT + "/" + id)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class DeleteProduct {
        @Test
        @DisplayName("DELETE /v1/products/{id} - SUCCESS")
        void shouldDeleteOneProductSuccessfully() throws Exception {
            final var id = UUID.randomUUID().toString();

            mockMvc.perform(delete(API_ENDPOINT + "/" + id)
                            .contentType(APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNoContent());
        }
    }
}
