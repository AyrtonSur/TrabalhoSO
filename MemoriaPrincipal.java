
import java.util.HashMap;
import java.util.Map;

class MemoriaPrincipal {
    private final int memoriaTotal = 32000;
    private int memoriaDisponivel;
    private Map<Integer,Integer> alocacao; //considerando que id é int porque imagino que vai ser gerado por incremento 
    // o ideal é fazer um map de processo com int, e fazer os metodos com objeto processo tbm, porem como nao tem processo ainda ele vai ficar dando pal
    public MemoriaPrincipal(){
        this.memoriaDisponivel = memoriaTotal;
        this.alocacao = new HashMap<>();
    }

    public int getMemoriaDisponivel() {
        return memoriaDisponivel;
    }

    public Map<Integer, Integer> getAlocacao() {
        return alocacao;
    }

    public synchronized void alocarMemoria(int idProcesso, int tamanho) {
        if (tamanho <= memoriaDisponivel) {
            alocacao.put(idProcesso, tamanho);
            memoriaDisponivel -= tamanho;       
            System.out.println("Memoria alocada");
        }
        System.out.println("Sem espaço na memoria para alocação");
    }
    
    public synchronized  void liberarmemoria(int idProcesso){
        if (alocacao.containsKey(idProcesso)){
            int tamanho = alocacao.get(idProcesso);
            alocacao.remove(idProcesso);
            memoriaDisponivel += tamanho;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Memoria Principal:");
        sb.append("memoriaTotal=").append(memoriaTotal);
        sb.append(", memoriaDisponivel=").append(memoriaDisponivel);
        sb.append(", alocacao=").append(alocacao);
        
        return sb.toString();
    }
}
