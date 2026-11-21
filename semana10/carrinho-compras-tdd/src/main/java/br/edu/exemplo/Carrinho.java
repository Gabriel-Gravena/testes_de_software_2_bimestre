package br.edu.exemplo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Carrinho {
    private final List<Produto> produtos = new ArrayList<>();

    public void adicionarProduto(Produto produto) {
        produtos.add(Objects.requireNonNull(produto, "Produto não pode ser nulo"));
    }

    public void removerProduto(Produto produto) {
        produtos.remove(produto);
    }

    public double getTotal() {
        double subtotal = produtos.stream().mapToDouble(Produto::getPreco).sum();
        return subtotal >= 500.0 ? subtotal * 0.9 : subtotal;
    }
}
