package br.edu.exemplo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class ContaBancariaAdicionalTest {
    @Test void saldoInicialZero() { assertEquals(0, new ContaBancaria().getSaldo(), 0.001); }
    @Test void ignoraValoresInvalidos() {
        ContaBancaria c = new ContaBancaria(); c.depositar(100); c.depositar(-1); c.depositar(0); c.sacar(-1); c.sacar(0);
        assertEquals(100, c.getSaldo(), 0.001);
    }
    @Test void permiteSacarTodoSaldo() { ContaBancaria c = new ContaBancaria(); c.depositar(100); c.sacar(100); assertEquals(0, c.getSaldo(), 0.001); }
    @Test void ignoraValoresNaoFinitos() {
        ContaBancaria c = new ContaBancaria(); c.depositar(10); c.depositar(Double.NaN); c.depositar(Double.POSITIVE_INFINITY); c.sacar(Double.NaN); c.sacar(Double.POSITIVE_INFINITY); assertEquals(10, c.getSaldo(), 0.001);
    }
}
