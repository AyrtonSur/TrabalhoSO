public class Processo {
    private static int idCounter = 0; /* Contador estático que define id do Projeto */
    private String id; /* id que recebe valor do Contador estático */
    private String [] transicaoDeEstados; /*Array que contem 2 elementos: [estadoAtual, estadoNovo] */
    private String faseAtual; /*Atributo que guarda a fase CPU 1, operacao de I/O ou fase CPU 2 */
    private int tempoFaseCpu1;
    private int tempoDuracaoEntradaSaida;
    private int tempoFaseCpu2;
    private final int quantidadeDeMemoriaRAM;

    public Processo(int quantidadeDeMemoriaRAM) {
        idCounter++;
        this.id = "" + idCounter;
        this.transicaoDeEstados = new String[2];
        this.transicaoDeEstados[0] = "Novo";
        this.transicaoDeEstados[1] = null;
        this.faseAtual = null;
        this.tempoFaseCpu1 = 0;
        this.tempoDuracaoEntradaSaida = 0;
        this.tempoFaseCpu2 = 0;
        this.quantidadeDeMemoriaRAM = quantidadeDeMemoriaRAM;
    }

    public void setTransicaoDeEstados(String estadoNovo) {
        if(transicaoDeEstados[0] == "Novo" && transicaoDeEstados[1] == null) {
            transicaoDeEstados[1] = estadoNovo;
        }
        else  {
            transicaoDeEstados[0] = transicaoDeEstados[1];
            transicaoDeEstados[1] = estadoNovo;
        }
    }

    public String getEstadoAtual() {
        return transicaoDeEstados[0];
    }

    public String getEstadoNovo() {
        return transicaoDeEstados[1];
    }


    public void setFaseAtual(String faseAtual) {
        this.faseAtual = faseAtual;
    }

    public String getFaseAtual() {
        return faseAtual;
    }

    public String getId() {
        return id;
    }

    public void setTempoFaseCpu1(int tempoFaseCpu1) {
        this.tempoFaseCpu1 = tempoFaseCpu1;
    }

    public int getTempoFaseCpu1() {
        return tempoFaseCpu1;
    }

    public void setTempoDuracaoEntradaSaida(int tempoDuracaoEntradaSaida) {
        this.tempoDuracaoEntradaSaida = tempoDuracaoEntradaSaida;
    }

    public int getTempoDuracaoEntradaSaida() {
        return tempoDuracaoEntradaSaida;
    }

    public void setTempoFaseCpu2(int tempoFaseCpu2) {
        this.tempoFaseCpu2 = tempoFaseCpu2;
    }

    public int getTempoFaseCpu2() {
        return tempoFaseCpu2;
    }

    public int getQuantidadeDeMemoriaRAM() {
        return quantidadeDeMemoriaRAM;
    }

    public String descritor () {
        String descritor =
                "ID Processo: " + id + " {\n" +
                        "   Duração de CPU fase 1: " + tempoFaseCpu1 + "\n" +
                        "   Duração de  I/O: " + tempoDuracaoEntradaSaida + "\n" +
                        "   Duração de CPU fase 2: " + tempoFaseCpu2 + "\n" +
                        "   Quantidade de Mbytes de RAM: " + quantidadeDeMemoriaRAM + "\n}"
                ;
        return descritor;
    }
}
