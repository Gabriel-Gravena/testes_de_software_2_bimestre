package br.edu.exemplo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ProdutoAdicionalTest {
    @Test void informaNome() { assertEquals("Teclado", new Produto("Teclado").getNome()); }
    @Test void ignoraEntradasNulas() { Produto p = new Produto("A"); p.adicionarEstoque(3); p.adicionarEstoque(0); assertEquals(3, p.getQuantidade()); }
    @Test void ignoraRetiradasNaoPositivas() { Produto p = new Produto("A"); p.adicionarEstoque(3); p.retirarEstoque(-1); p.retirarEstoque(0); assertEquals(3, p.getQuantidade()); }
    @Test void permiteRetirarTudo() { Produto p = new Produto("A"); p.adicionarEstoque(3); p.retirarEstoque(3); assertEquals(0, p.getQuantidade()); }
}
