package br.uff.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    ProductsUseCases productsUseCases;

    @Test
    @DisplayName("POST /v1/products - SUCCESS")
    void shouldCreateProductSuccessfully() throws Exception {
        final var product = new Product();
        final var requestBody = objectMapper.writeValueAsString(product);
        final var id = UUID.randomUUID();

        doReturn(id).when(productsUseCases).createProduct(any());

        mockMvc.perform(post(API_ENDPOINT)
                        .contentType(APPLICATION_JSON)
                        .content(requestBody))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(id.toString())));
    }
}
