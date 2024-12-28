import java.util.concurrent.TimeUnit;

class DispositivoES implements Runnable {
  private final Fila<Descritor> filaAuxiliar; // Fila que os Descritors seram enviados para
  private final Descritor descritor;

  public DispositivoES(Descritor descritor, Fila<Descritor> auxiliar) {
    this.descritor = descritor;
    this.filaAuxiliar = auxiliar;
  }

  @Override
  public void run() {
    if (descritor != null) {
        System.out.println("Descritor " + descritor.getId() + " iniciou operação de E/S por " +
                descritor.getTempoDuracaoEntradaSaida());

        // Simula o tempo de E/S
        try {
            TimeUnit.MILLISECONDS.sleep(descritor.getTempoDuracaoEntradaSaida() * 100); // Multiplica para ajustar unidades de tempo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.println("Descritor " + descritor.getId() + " finalizou operação de E/S.");

        // Marca o Descritor como "pronto" e move para a fila de prontos
        Processo processo = MemoriaPrincipal.getInstance().getprocesso(descritor.getId());
        processo.setTransicaoDeEstados("Pronto");
        processo.setFaseAtual("FaseCPU2");
        processo.setTempoDuracaoEntradaSaida(0);
        filaAuxiliar.adicionar(descritor);
    } else {
        // Aguarda brevemente antes de verificar novamente a fila de bloqueados
        try {
            TimeUnit.MILLISECONDS.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
      
  }
}