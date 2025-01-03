import java.util.ArrayList;

public class ObservadorEstados {
    private static final Object LOCKERARRAYCPU = new Object();
    private static final Object LOCKERARRAYDISPOSITIVOSES = new Object();
    private static final ArrayList<CPU> arrayCPU = new ArrayList<>();
    private static final ArrayList<DispositivoES> arrayDispositivosES = new ArrayList<>();

    public static void adicionarCPU(CPU novaCPU) {
        if (novaCPU == null) return;
        synchronized (ObservadorEstados.LOCKERARRAYCPU) {
            ObservadorEstados.arrayCPU.add(novaCPU);
        }
    }

    public static void limparArrayCPUS() {
        synchronized (ObservadorEstados.LOCKERARRAYCPU) {
            ObservadorEstados.arrayCPU.clear();
        }
    }

    public static void removerArrayCPUS(CPU cpu) {
        if (cpu == null) return;
        synchronized (ObservadorEstados.LOCKERARRAYCPU) {
            ObservadorEstados.arrayCPU.remove(cpu);
        }
    }

    public static void adicionarDispositivoES(DispositivoES novoDispositivoES) {
        if (novoDispositivoES == null) return;
        synchronized (ObservadorEstados.LOCKERARRAYDISPOSITIVOSES) {
            ObservadorEstados.arrayDispositivosES.add(novoDispositivoES);
        }
    }

    public static void limparArrayDispositivosES() {
        synchronized (ObservadorEstados.LOCKERARRAYDISPOSITIVOSES) {
            ObservadorEstados.arrayDispositivosES.clear();
        }
    }

    public static void removerArrayDispositivosES(DispositivoES dispositivo) {
        if (dispositivo == null) return;
        synchronized (ObservadorEstados.LOCKERARRAYDISPOSITIVOSES) {
            ObservadorEstados.arrayDispositivosES.remove(dispositivo);
        }
    }

    public synchronized static void exibirEstadosCPUS() {
        int contador = 0;
        synchronized (ObservadorEstados.LOCKERARRAYCPU) {
            for (CPU currentCPU : ObservadorEstados.arrayCPU) {
                contador++;
                Descritor descritorCPU = currentCPU.getDescritor();
                if (descritorCPU == null) {
                    System.out.println("- CPU " + contador + " ociosa");
                    continue;
                }
                System.out.println("- CPU " + contador + " executando processo " +
                descritorCPU.getId() + " em fase: " + descritorCPU.getFaseAtual());
            }
        }

        System.out.printf("\n");

        contador = 0;
        synchronized (ObservadorEstados.LOCKERARRAYDISPOSITIVOSES) {
            for (DispositivoES currentDispositivoES : ObservadorEstados.arrayDispositivosES) {
                contador++;
                Descritor descritorDispositivoES = currentDispositivoES.getDescritor();
                if (descritorDispositivoES == null) continue;
                System.out.println("- Dispositivo ES " + contador + " executando processo " +
                descritorDispositivoES.getId());
            }
        }
    }
}
