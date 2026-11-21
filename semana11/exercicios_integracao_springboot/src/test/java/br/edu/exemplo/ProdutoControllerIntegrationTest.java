package br.edu.exemplo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Exercício 2 — integração HTTP simulada com MockMvc.
 * Fluxo exercitado: DispatcherServlet → ProdutoController → ProdutoService
 * → ProdutoRepository → H2, sem mocks e sem servidor HTTP real.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProdutoControllerIntegrationTest {

    private static final String PRODUTO_JSON = """
            {"nome":"Mouse","preco":80.0}
            """;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    /** CT04 — Cadastro via API. */
    @Test
    void deveCadastrarProdutoPelaApi() throws Exception {
        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUTO_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.preco").value(80.0));
    }

    /** CT05 — Consulta via API. */
    @Test
    void deveBuscarProdutoPelaApi() throws Exception {
        MvcResult respostaPost = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(PRODUTO_JSON))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode corpo = objectMapper.readTree(respostaPost.getResponse().getContentAsString());
        assertTrue(corpo.get("id").isNumber(), "A resposta do POST deve conter um ID numérico");
        long id = corpo.get("id").asLong();

        mockMvc.perform(get("/produtos/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nome").value("Mouse"))
                .andExpect(jsonPath("$.preco").value(80.0));
    }
}
