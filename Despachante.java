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
    while (!Thread.currentThread().isInterrupted()) {
      Descritor descritor;
      synchronized (this.filaAuxiliar) {
        descritor = filaAuxiliar.remover();
      }
      if (descritor == null) {
        synchronized (this.filaProntos) {
          descritor = filaProntos.remover();
        }
      }
  
      if (descritor != null) {
        Processo processo = memoriaPrincipal.getprocesso(descritor.getId());
        boolean alocado = false;

        if (processo.getFaseAtual().equals("FaseCPU1") && processo.getTempoFaseCpu1() == 0) {
          if (processo.getTempoDuracaoEntradaSaida() == 0) {
            processo.setFaseAtual("Finalizado");
            processo.getDescritor().setFaseAtual("Finalizado");
            processo.setTransicaoDeEstados("Finalizado");
            alocado = true;
            memoriaPrincipal.liberarmemoria(processo);
          } else {
            processo.setFaseAtual("FaseEntradaSaida");
            processo.getDescritor().setFaseAtual("FaseEntradaSaida");
            processo.setTransicaoDeEstados("Bloqueado");
            alocado = true;
            DispositivoES ES = new DispositivoES(descritor, filaAuxiliar);
            ObservadorEstados.adicionarDispositivoES(ES);
            Thread threadES = new Thread(ES);
            threadES.setDaemon(true);
            threadES.start();
          }
        } else if (processo.getFaseAtual().equals("FaseCPU2") && processo.getTempoFaseCpu2() == 0) {
          processo.setFaseAtual("Finalizado");
          processo.getDescritor().setFaseAtual("Finalizado");
          processo.setTransicaoDeEstados("Finalizado");
          alocado = true;
          memoriaPrincipal.liberarmemoria(processo);
        }

        while(!alocado) {
          for (CPU cpu : cpus) {
            if (cpu.getDescritor() == null) {
              processo.setTransicaoDeEstados("Executando");
              cpu.execute(descritor, filaProntos, Despachante.QUANTUM, descritor.getFaseAtual());
              alocado = true;
              break;
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
