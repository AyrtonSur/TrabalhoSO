import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class Fila <T> implements Iterable<T> {
    private Queue<T> fila;
    private int tamanhoMaximo;

    public Fila() {
        this.fila = new LinkedList<>();
        this.tamanhoMaximo = Integer.MAX_VALUE;
    }

    public Fila(int tamanhoMaximo) {
        this.fila = new LinkedList<>();
        if(tamanhoMaximo > 0) {
            this.tamanhoMaximo = tamanhoMaximo;
        } else {
            this.tamanhoMaximo = Integer.MAX_VALUE;
        }
    }

    public String adicionar(T processo) {
         if(fila.size() < tamanhoMaximo) {
             fila.add(processo);
             return "Processo adicionado na fila";
         } else {
             return "Fila cheia";
         }
    }

    public T remover() {
        return fila.poll();
    }

    public T visualizar(){
        return fila.peek();
    }

    public int tamanho() {
        return fila.size();
    }

    public boolean estaVazia() {
        return fila.isEmpty();
    }

    public boolean estaCheia() {
        return fila.size() == tamanhoMaximo;
    }

    public Iterator<T> iterator() {
        return fila.iterator(); // Return the LinkedList iterator
    }

    public String toString() {
        return "Fila: " + fila.toString() + " (Tamanho Máximo: " + tamanhoMaximo + ")";
    }
}
