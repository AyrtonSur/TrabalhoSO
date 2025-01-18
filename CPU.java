import java.util.concurrent.TimeUnit;

public class CPU implements Runnable {
    private static final long DURACAOQUANTUMMS = 1000L;
    private Descritor descritor;
    private Fila<Descritor> filaProntos;
    private int quantum;
    private boolean ativo = true;

    public Descritor getDescritor() { return descritor; }

    private void atualizarTempoDescritor(Descritor descritor, int quantum, String faseAtual) {
        descritor.setFaseAtual(faseAtual);
        if("FaseCPU1".equals(faseAtual)) {
            if(descritor.getTempoFaseCpu1() < quantum) {
                this.quantum = descritor.getTempoFaseCpu1();
                descritor.setTempoFaseCpu1(0);
            } else {
                descritor.setTempoFaseCpu1(descritor.getTempoFaseCpu1() - quantum);
            }

        } else if("FaseCPU2".equals(faseAtual)) {
            if(descritor.getTempoFaseCpu2() < quantum) {
                this.quantum = descritor.getTempoFaseCpu2();
                descritor.setTempoFaseCpu2(0);
            } else {
                descritor.setTempoFaseCpu2(descritor.getTempoFaseCpu2() - quantum);
            }

        }
    }

    public synchronized void execute(Descritor descritor, Fila<Descritor> filaProntos, int quantum, String faseAtual){
        this.descritor = descritor;
        this.filaProntos = filaProntos;
        this.quantum = quantum;
        atualizarTempoDescritor(descritor, quantum, faseAtual);
        notify();
    }

    public void run() {
        while(!Thread.currentThread().isInterrupted() && ativo) {
            synchronized(this) {
                while(descritor == null && ativo) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            if(descritor != null) {
                try {
                    // System.out.println("Executando processo " + descritor.getId() + " com tempo de " + quantum * 100 + " milisegundos.");
                    TimeUnit.MILLISECONDS.sleep(this.quantum * CPU.DURACAOQUANTUMMS);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    liberarCPU();
                }
            }
        }
    }

    private synchronized void liberarCPU() {
        // System.out.println("CPU liberada após executar o processo.");
        this.descritor.setTransicaoDeEstados("pronto");
        this.filaProntos.adicionar(this.descritor);
        this.descritor = null;
        this.quantum = 0;
    }

    public void encerrar() {
        synchronized (this) {
            ativo = false;
            notify();
        }
    }


}
