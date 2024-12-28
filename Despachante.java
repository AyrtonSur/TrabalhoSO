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
      Descritor descritor = filaAuxiliar.remover();
      if (descritor == null) {
        descritor = filaProntos.remover();
      }
  
      if (descritor != null) {
        Processo processo = memoriaPrincipal.getprocesso(descritor.getId());
        boolean alocado = false;

        if (processo.getFaseAtual().equals("FaseCpu1")  && processo.getTempoFaseCpu1() == 0) {
          if (processo.getTempoDuracaoEntradaSaida() == 0) {
            processo.setFaseAtual("Finalizado");
            processo.setTransicaoDeEstados("finalizado");
            alocado = true;
            memoriaPrincipal.liberarmemoria(processo);
          } else {
            processo.setFaseAtual("FaseEntradaSaida");
            processo.setTransicaoDeEstados("bloqueado");
            alocado = true;
            DispositivoES ES = new DispositivoES(descritor, filaAuxiliar);
            Thread threadES = new Thread(ES);
            threadES.start();
          }
        } else if (processo.getFaseAtual().equals("FaseCpu2") && processo.getTempoFaseCpu2() == 0) {
          processo.setFaseAtual("Finalizado");
          processo.setTransicaoDeEstados("finalizado");
          alocado = true;
          memoriaPrincipal.liberarmemoria(processo);
        }

  

        while(!alocado) {
          for (CPU cpu : cpus) {
            if (cpu.getDescritor() == null) {
              processo.setTransicaoDeEstados("executando");
              cpu.execute(descritor, filaProntos, QUANTUM, descritor.getFaseAtual());
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
