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
        System.out.printf("Descritor #%d iniciou operação de E/S por %d u.t.%n",
                descritor.getId(), descritor.getTempoDuracaoEntradaSaida());

        // Simula o tempo de E/S
        try {
            TimeUnit.MILLISECONDS.sleep(descritor.getTempoDuracaoEntradaSaida() * 100); // Multiplica para ajustar unidades de tempo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.printf("Descritor #%d finalizou operação de E/S.%n", descritor.getId());

        // Marca o Descritor como "pronto" e move para a fila de prontos
        descritor.setTransicaoDeEstados("pronto");
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