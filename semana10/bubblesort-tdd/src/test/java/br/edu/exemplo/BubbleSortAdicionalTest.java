package br.edu.exemplo;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class BubbleSortAdicionalTest {
    @Test void ordenaVazioEUnitario() {
        BubbleSort vazio = new BubbleSort(new int[]{}); vazio.ordenar(); assertArrayEquals(new int[]{}, vazio.getNumeros());
        BubbleSort unico = new BubbleSort(new int[]{7}); unico.ordenar(); assertArrayEquals(new int[]{7}, unico.getNumeros());
    }
    @Test void ordenaInversoENegativos() {
        BubbleSort sort = new BubbleSort(new int[]{4, 0, -1, -5}); sort.ordenar(); assertArrayEquals(new int[]{-5, -1, 0, 4}, sort.getNumeros());
    }
}
