public class Processo {
    private static int idCounter = 0; /* Contador estático que define id do Projeto */
    private String id; /* id que recebe valor do Contador estático */
    private String [] transicaoDeEstados; /*Array que contem 2 elementos: [estadoAtual, estadoNovo] */
    private Descritor descritor;

    public Processo(int quantidadeDeMemoriaRAM) {
        idCounter++;
        this.id = "" + idCounter;
        this.transicaoDeEstados = new String[2];
        this.transicaoDeEstados[0] = "Novo";
        this.transicaoDeEstados[1] = null;
        this.descritor = new Descritor(id, transicaoDeEstados, null, 0, 0, 0, quantidadeDeMemoriaRAM);
    }

    public void setTransicaoDeEstados(String estadoNovo) {
        if(transicaoDeEstados[0] == "Novo" && transicaoDeEstados[1] == null) {
            transicaoDeEstados[1] = estadoNovo;
        }
        else  {
            transicaoDeEstados[0] = transicaoDeEstados[1];
            transicaoDeEstados[1] = estadoNovo;
        }
        descritor.setVetorTransicaoDeEstados(transicaoDeEstados);
    }

    public String getEstadoAtual() {
        return transicaoDeEstados[0];
    }

    public String getEstadoNovo() {
        return transicaoDeEstados[1];
    }


    public void setFaseAtual(String faseAtual) {
        descritor.setFaseAtual(faseAtual);
    }

    public String getFaseAtual() {
        return descritor.getFaseAtual();
    }

    public String getId() {
        return id;
    }

    public void setTempoFaseCpu1(int tempoFaseCpu1) {
        descritor.setTempoFaseCpu1(tempoFaseCpu1);
    }

    public int getTempoFaseCpu1() {
        return descritor.getTempoFaseCpu1();
    }

    public void setTempoDuracaoEntradaSaida(int tempoDuracaoEntradaSaida) {
        descritor.setTempoDuracaoEntradaSaida(tempoDuracaoEntradaSaida);
    }

    public int getTempoDuracaoEntradaSaida() {
        return descritor.getTempoDuracaoEntradaSaida();
    }

    public void setTempoFaseCpu2(int tempoFaseCpu2) {
        descritor.setTempoFaseCpu2(tempoFaseCpu2);
    }

    public int getTempoFaseCpu2() {
        return descritor.getTempoFaseCpu2();
    }

    public int getQuantidadeDeMemoriaRAM() {
        return descritor.getQuantidadeDeMemoriaRAM();
    }

    public Descritor getDescritor() {
        return descritor;
    }
}
