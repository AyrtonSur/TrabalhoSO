import java.util.concurrent.TimeUnit;

class Despachante implements Runnable {
  private final Fila<Descritor> filaProntos;
  private final Fila<Descritor> filaAuxiliar;
  private MemoriaPrincipal memoriaPrincipal;
  private final CPU[] cpus;
  private static final int QUANTUM = 4;

  public Despachante(Fila<Descritor> prontos, Fila<Descritor> auxiliar, CPU[] cpus) {
    this.filaProntos = prontos;
    this.filaAuxiliar = auxiliar;
    this.cpus = cpus;
    this.memoriaPrincipal = MemoriaPrincipal.getInstance();
  }

  @Override
  public void run() {
    while (true) {
      for (CPU cpu : cpus) {
        while (cpu.getDescritor() == null) { // Enquanto cpu estiver livre loop irá se repetir até que um descritor seja alocado para CPU
          Descritor descritor = filaAuxiliar.remover();
          if (descritor != null) { // verifica se existe um descritor na filaAuxiliar dando preferência para a fila
            Processo processo = memoriaPrincipal.getprocesso(descritor.getId());
            processo.setTransicaoDeEstados("executando"); // Descritor passa para estado executando
            cpu.execute(descritor, QUANTUM, descritor.getFaseAtual()); // Descritor é entregue para a cpu para começar a executar
          } else { // para o caso em que não tinha Descritors na filaAuxiliar
            descritor = filaProntos.remover(); // pega o Descritor da fila de prontos
            if (descritor != null) { // tem Descritor na fila de prontos
              Processo processo = memoriaPrincipal.getprocesso(descritor.getId());
              processo.setTransicaoDeEstados("executando");
              cpu.execute(descritor, QUANTUM, descritor.getFaseAtual());
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
