package br.edu.exemplo;

import br.edu.exemplo.model.Produto;
import br.edu.exemplo.repository.ProdutoRepository;
import br.edu.exemplo.service.ProdutoService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Exercício 1 — integração entre ProdutoService, ProdutoRepository e o banco H2.
 * Nenhum componente é simulado: o contexto Spring completo é carregado e cada
 * teste roda dentro de uma transação que sofre rollback ao final.
 */
@SpringBootTest
@Transactional
class ProdutoServiceIntegrationTest {

    @Autowired
    ProdutoService service;

    @Autowired
    ProdutoRepository repository;

    @Autowired
    EntityManager entityManager;

    /** CT01 — Cadastro válido. */
    @Test
    void deveCadastrarERecuperarProduto() {
        Produto salvo = service.cadastrar("Teclado", 150.0);

        assertNotNull(salvo.getId(), "O produto cadastrado deve receber um ID");

        Produto doBanco = repository.findById(salvo.getId())
                .orElseThrow(() -> new AssertionError("Produto não encontrado no repositório"));

        assertEquals("Teclado", doBanco.getNome());
        assertEquals(150.0, doBanco.getPreco(), 0.0001);
    }

    /** CT02 — Nome inválido. */
    @Test
    void naoDevePersistirProdutoComNomeEmBranco() {
        long quantidadeAntes = repository.count();

        IllegalArgumentException erro = assertThrows(IllegalArgumentException.class,
                () -> service.cadastrar("   ", 80.0));

        assertEquals("Nome obrigatório", erro.getMessage());
        assertEquals(quantidadeAntes, repository.count(),
                "Nenhum registro deve ser inserido quando o nome é inválido");
    }

    /** CT03 — Atualização de preço (desafio). */
    @Test
    void deveAtualizarPrecoPersistido() {
        Produto mouse = service.cadastrar("Mouse", 80.0);

        service.atualizarPreco(mouse.getId(), 95.0);

        // flush + clear força o Hibernate a gravar no H2 e descartar o cache de
        // primeiro nível, garantindo que a consulta abaixo leia do banco.
        entityManager.flush();
        entityManager.clear();
        Produto doBanco = repository.findById(mouse.getId())
                .orElseThrow(() -> new AssertionError("Produto não encontrado no repositório"));

        assertEquals(95.0, doBanco.getPreco(), 0.0001);
        assertEquals("Mouse", doBanco.getNome());
    }
}
