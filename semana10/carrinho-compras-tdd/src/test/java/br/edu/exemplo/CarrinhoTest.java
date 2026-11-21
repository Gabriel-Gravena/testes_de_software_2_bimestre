package br.edu.exemplo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarrinhoTest {
    @Test void carrinhoVazioTemTotalZero() { assertEquals  (0.0, new Carrinho().getTotal(), 0.00001); }
    @Test void adicionaUmProduto() {
        Carrinho c = new Carrinho(); Produto p = new Produto("Teclado", 100);
        assertEquals("Teclado", p.getNome()); c.adicionarProduto(p); assertEquals(100, c.getTotal(), 0.00001);
    }
    @Test void somaVariosProdutos() {
        Carrinho c = new Carrinho(); c.adicionarProduto(new Produto("Teclado", 100)); c.adicionarProduto(new Produto("Mouse", 50));
        assertEquals(150, c.getTotal(), 0.00001);
    }
    @Test void removeProduto() {
        Carrinho c = new Carrinho(); Produto mouse = new Produto("Mouse", 50);
        c.adicionarProduto(new Produto("Teclado", 100)); c.adicionarProduto(mouse); c.removerProduto(mouse);
        assertEquals(100, c.getTotal(), 0.00001);
    }
    @Test void rejeitaPrecoNegativo() { assertThrows(IllegalArgumentException.class, () -> new Produto("Teclado", -100)); }
    @Test void rejeitaPrecoNaoFinito() { assertThrows(IllegalArgumentException.class, () -> new Produto("A", Double.NaN)); assertThrows(IllegalArgumentException.class, () -> new Produto("A", Double.POSITIVE_INFINITY)); }
    @Test void rejeitaProdutoNulo() { assertThrows(NullPointerException.class, () -> new Carrinho().adicionarProduto(null)); }
    @Test void aceitaPrecoZero() { assertEquals(0, new Produto("Gratis", 0).getPreco(), 0.00001); }
    @Test void descontoNaFronteira() {
        Carrinho abaixo = new Carrinho(); abaixo.adicionarProduto(new Produto("A", 499.99)); assertEquals(499.99, abaixo.getTotal(), 0.00001);
        Carrinho igual = new Carrinho(); igual.adicionarProduto(new Produto("B", 500)); assertEquals(450, igual.getTotal(), 0.00001);
        Carrinho acima = new Carrinho(); acima.adicionarProduto(new Produto("C", 500.01)); assertEquals(450.009, acima.getTotal(), 0.00001);
    }
    @Test void descontoRecalculadoAposRemocao() {
        Carrinho c = new Carrinho(); Produto p = new Produto("B", 100); c.adicionarProduto(new Produto("A", 450)); c.adicionarProduto(p);
        assertEquals(495, c.getTotal(), 0.00001); c.removerProduto(p); assertEquals(450, c.getTotal(), 0.00001);
    }
}

