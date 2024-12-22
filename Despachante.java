import java.util.concurrent.TimeUnit;

class Despachante implements Runnable {
  private final Fila<Processo> filaProntos;
  private final Fila<Processo> filaAuxiliar;
  private final CPU[] cpus;
  private static final int QUANTUM = 4;

  public Despachante(Fila<Processo> prontos, Fila<Processo> auxiliar, CPU[] cpus) {
    this.filaProntos = prontos;
    this.filaAuxiliar = auxiliar;
    this.cpus = cpus;
  }

  @Override
  public void run() {
    while (true) {
      for (CPU cpu : cpus) {
        while (cpu.getProcesso() == null) { // Enquanto cpu estiver livre loop irá se repetir até que um processo seja alocado para CPU
          if ((Processo processo = filaAuxiliar.remover()) != null) { // verifica se existe um processo na filaAuxiliar dando preferência para a fila
            processo.setTransicaoDeEstados("executando"); // processo passa para estado executando
            cpu.execute(processo, QUANTUM); // processo é entregue para a cpu para começar a executar
          } else { // para o caso em que não tinha processos na filaAuxiliar
            Processo processo = filaProntos.remover(); // pega o processo da fila de prontos
            if (processo != null) { // tem processo na fila de prontos
              processo.setTransicaoDeEstados("executando");
              cpu.execute(processo, QUANTUM);
            }
          }
        }
      }

      try {
        TimeUnit.MILLISECONDS.sleep(50); // Simula controle periódico
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
    }
  }
}
