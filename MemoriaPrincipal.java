

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class MemoriaPrincipal {
    private final int memoriaTotal = 32000;
    private Collection<Processo> processos;
    private HashMap<String, int[]> menorEmaior;
    private byte[] alocacao;
    public MemoriaPrincipal(){      
        this.processos = new ArrayList<>();
        this.alocacao = new byte[32000];
        this.menorEmaior = new HashMap<>();
        
    }

    
    public synchronized void alocarMemoria(Processo processo, int tamanho) {
        boolean alocado = false;
        int countAlocacao = 0;
        int espacolivre = 0;
        while (alocado==false && countAlocacao<32000){
            if (alocacao[countAlocacao]== (byte) 0){
                espacolivre +=1;
            }
            if (alocacao[countAlocacao]== (byte) 1){
                espacolivre = 0;
            }
            if (espacolivre==tamanho){
                for(int i=(countAlocacao+1)-tamanho;i<=countAlocacao;i++){
                    alocacao[i] = 1;
                }
                menorEmaior.put(processo.getId(), new int[]{(countAlocacao+1)-tamanho, countAlocacao+1});
                processos.add(processo);
                alocado = true;
            }
            countAlocacao+=1;
        }

        if (alocado==true) {      
            int[] valores = menorEmaior.get(processo.getId());
            System.out.println("Memoria alocada para o processo"+ processo.getId() + "no espaço de" + valores[0] + "-" + valores[1]);
        }
        else{
            System.out.println("Não foi possível alocar a memória");
        }
    }
    
    public synchronized  void liberarmemoria(Processo processo){
        int[] valores = menorEmaior.get(processo.getId());
        if (valores!=null){
            int inicio = valores[0];
            int finall = valores[1];

            for (int i = inicio; i<finall ;i++){
                alocacao[i]=0;
            }

            menorEmaior.remove(processo.getId());
            processos.remove(processo);
            System.out.println("Memoria desalocada");
            arrumarmemoria();
        }
        else{
            System.out.println("Este processo não foi alocado anteriormente");
        }
        
        
    }
    public synchronized  void arrumarmemoria(){
        boolean arrumado=false;
        int iterador = -1;
        int iteradorProcuraAlocacao = 0;
        int finalprocesso = 0;
        int comecoprocesso = 0;
        int tamanhoprocesso = 0;
        while(iterador<32000){
            iterador+=1;
            if (alocacao[iterador] == 1){
                continue;
            }
            if (alocacao[iterador] == 0){
                iteradorProcuraAlocacao = iterador;
                while(arrumado==false){
                    if (alocacao[iteradorProcuraAlocacao] == 0){
                        iteradorProcuraAlocacao+=1;
                        continue;
                    }
                    if (alocacao[iteradorProcuraAlocacao] == 1){
                         finalprocesso = getprocessomenor(iteradorProcuraAlocacao);
                         comecoprocesso = iteradorProcuraAlocacao;
                         tamanhoprocesso = (finalprocesso - comecoprocesso) +1;
                         for (int i=comecoprocesso;i<=finalprocesso;i++){
                            alocacao[i] = 0;
                         }
                         arrumado=true;

                    }
                }

                for(int i = iterador;i< iterador+tamanhoprocesso;i++){
                    alocacao[i] = 1;
                }
                finalprocesso = 0;
                comecoprocesso = 0;
                tamanhoprocesso = 0;
                arrumado=false;
                iteradorProcuraAlocacao = 0;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Memoria Principal:");
        sb.append("Memoria Total=").append(memoriaTotal);
        sb.append(", Processos alocados=").append(processos);
        
        return sb.toString();
    }

    public int getMemoriaTotal() {
        return memoriaTotal;
    }

    public Processo getprocesso(String Idprocesso){
        Processo processoBusca = null;
        for (Processo p : processos) {
            if (p.getId() == Idprocesso) { 
                processoBusca = p;
                break; 
            }
        }
        return processoBusca;
    }

    public int getprocessomenor(int menor){
        
        for (Map.Entry<String, int[]> entrada : menorEmaior.entrySet()) {
            int[] lista = entrada.getValue(); 
            if (lista[0] == menor) { 
                //chave=entrada.getkey();      
                return lista[1];             
            }
        }
        return -1;
    }

    public Collection<Processo> getProcessos() {
        return processos;
    }
}
