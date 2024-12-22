import java.util.concurrent.TimeUnit;

public class CPU implements Runnable {
    private Descritor descritor;
    private int quantum;
    private String faseAtual;
    private boolean ativo = true;

    public synchronized Descritor getDescritor() {
        return descritor;
    }

    private void atualizarTempoDescritor(Descritor descritor, int quantum, String faseAtual) {
        if("Fase CPU 1".equals(faseAtual)) {
            descritor.setTempoFaseCpu1(descritor.getTempoFaseCpu1() + quantum);

        } else if("Fase CPU 2".equals(faseAtual)) {
            descritor.setTempoFaseCpu2(descritor.getTempoFaseCpu2() + quantum);
        }
    }

    public synchronized void execute(Descritor descritor, int quantum, String faseAtual){
        if(descritor == null){
            this.descritor = descritor;
            this.quantum = quantum;
            this.faseAtual = faseAtual;
            atualizarTempoDescritor(descritor, quantum, faseAtual);
            notify();
        }
    }

    public void run() {
        while(ativo) {
            synchronized(this) {
                while(descritor == null & ativo) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
            if(descritor != null) {
                try{
                    System.out.println("Executando processo " + descritor.getId() + " com quantum de " + quantum * 100 + " milisegundos.");
                    TimeUnit.MILLISECONDS.sleep(quantum * 100L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    liberarCPU();
                }
            }
        }
    }

    private synchronized void liberarCPU() {
        System.out.println("CPU liberada após executar o processo.");
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
