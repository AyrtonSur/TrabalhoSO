import java.util.concurrent.TimeUnit;

class DispositivoES implements Runnable {
  private final Fila<Processo> filaAuxiliar; // Fila que os processos seram enviados para
  private final Processo processo;

  public DispositivoES(Processo processo, Fila<Processo> auxiliar) {
      this.processo = processo;
      this.filaAuxiliar = auxiliar;
  }

  @Override
  public void run() {
    if (processo != null) {
        System.out.printf("Processo #%d iniciou operação de E/S por %d u.t.%n",
                processo.getId(), processo.getTempoDuracaoEntradaSaida());

        // Simula o tempo de E/S
        try {
            TimeUnit.MILLISECONDS.sleep(processo.getTempoDuracaoEntradaSaida() * 100); // Multiplica para ajustar unidades de tempo
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }

        System.out.printf("Processo #%d finalizou operação de E/S.%n", processo.getId());

        // Marca o processo como "pronto" e move para a fila de prontos
        processo.setTransicaoDeEstados("pronto");
        filaAuxiliar.adicionar(processo);
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