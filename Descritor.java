import java.util.ArrayList;

public class Descritor {
    private String id;
    private String faseAtual;
    private int tempoFaseCpu1;
    private int tempoDuracaoEntradaSaida;
    private int tempoFaseCpu2;
    private final int quantidadeDeMemoriaRAM;


    public Descritor(String id, String faseAtual, int tempoFaseCpu1, int tempoDuracaoEntradaSaida, int tempoFaseCpu2, int quantidadeDeMemoriaRAM) {
        this.id = id;
        this.faseAtual = faseAtual;
        this.tempoFaseCpu1 = tempoFaseCpu1;
        this.tempoDuracaoEntradaSaida = tempoDuracaoEntradaSaida;
        this.tempoFaseCpu2 = tempoFaseCpu2;
        this.quantidadeDeMemoriaRAM = quantidadeDeMemoriaRAM;
    }

    public String getId() {
        return id;
    }

    public String getFaseAtual() {
        return faseAtual;
    }

    public void setFaseAtual(String faseAtual) {
        this.faseAtual = faseAtual;
    }

    public int getTempoFaseCpu1() {
        return tempoFaseCpu1;
    }

    public void setTempoFaseCpu1(int tempoFaseCpu1) {
        this.tempoFaseCpu1 = tempoFaseCpu1;
    }

    public int getTempoDuracaoEntradaSaida() {
        return tempoDuracaoEntradaSaida;
    }

    public void setTempoDuracaoEntradaSaida(int tempoDuracaoEntradaSaida) {
        this.tempoDuracaoEntradaSaida = tempoDuracaoEntradaSaida;
    }

    public int getTempoFaseCpu2() {
        return tempoFaseCpu2;
    }

    public void setTempoFaseCpu2(int tempoFaseCpu2) {
        this.tempoFaseCpu2 = tempoFaseCpu2;
    }

    public int getQuantidadeDeMemoriaRAM() {
        return quantidadeDeMemoriaRAM;
    }

    public String toString() {
        String descritor =
                "ID Processo: " + id + " {\n" +
                        "   Fase Atual: " + faseAtual + "\n" +
                        "   Duração de CPU fase 1: " + tempoFaseCpu1 + "\n" +
                        "   Duração de  I/O: " + tempoDuracaoEntradaSaida + "\n" +
                        "   Duração de CPU fase 2: " + tempoFaseCpu2 + "\n" +
                        "   Quantidade de Mbytes de RAM: " + quantidadeDeMemoriaRAM + "\n}"
                ;
        return descritor;
    }

}
