public class Descritor {
    private String id;
    private String faseAtual;
    public String [] transicaoDeEstados;
    private int tempoFaseCpu1;
    private int tempoDuracaoEntradaSaida;
    private int tempoFaseCpu2;
    private final int quantidadeDeMemoriaRAM;


    public Descritor(String id, String [] transicaoDeEstados, String faseAtual, int tempoFaseCpu1, int tempoDuracaoEntradaSaida, int tempoFaseCpu2, int quantidadeDeMemoriaRAM) {
        this.id = id;
        this.transicaoDeEstados = transicaoDeEstados;
        this.faseAtual = faseAtual;
        this.tempoFaseCpu1 = tempoFaseCpu1;
        this.tempoDuracaoEntradaSaida = tempoDuracaoEntradaSaida;
        this.tempoFaseCpu2 = tempoFaseCpu2;
        this.quantidadeDeMemoriaRAM = quantidadeDeMemoriaRAM;
    }

    public String getId() {
        return id;
    }

    public String [] getTransicaoDeEstados() {
        return transicaoDeEstados;
    }

    public String getEstadoAtual() {
        return transicaoDeEstados[0];
    }

    public String getEstadoNovo() {
        return transicaoDeEstados[1];
    }

    public void setVetorTransicaoDeEstados(String [] transicaoDeEstados) {
        this.transicaoDeEstados = transicaoDeEstados;
    }

    public void setTransicaoDeEstados(String estadoNovo) {
        MemoriaPrincipal mp = MemoriaPrincipal.getInstance();
        Processo p = mp.getprocesso(id);
        p.setTransicaoDeEstados(estadoNovo);
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
