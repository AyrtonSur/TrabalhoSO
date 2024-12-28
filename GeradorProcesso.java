import java.lang.Runnable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Scanner;
import java.util.InputMismatchException;

public class GeradorProcesso implements Runnable {
    private static final String MENSAGEMERRO1 = "Erro: Informe um valor positivo maior que zero.";
    private static final String MENSAGEMERRO2 = "Erro: Informe um valor positivo maior ou igual a zero.";
    private Deque<Processo> filaProcessos;
    private Fila<Descritor> filaProntos;
    private MemoriaPrincipal memoriaP;

    public GeradorProcesso(Fila<Descritor> filaProntos, MemoriaPrincipal memoriaP) {
        this.filaProcessos = new ArrayDeque<Processo>();
        this.filaProntos = filaProntos;
        this.memoriaP = memoriaP;
    }

    private void adicionarProcesso(Processo p) {
        synchronized(this.filaProcessos) {
            this.filaProcessos.addLast(p);
            System.out.println("Processo adicionado: " + p);
            for (Descritor descritor : filaProntos) {
                System.out.println("ID: " + descritor.getId());
            }

        }
    }

    private void processarProcessos() {
        if (this.filaProcessos.peekFirst() != null) {
            Processo processoAtual;
            synchronized (this.filaProcessos) {
                processoAtual = this.filaProcessos.pollFirst();
            }
            synchronized (this.filaProntos) {
                this.filaProntos.adicionar(processoAtual.getDescritor());
            }
            synchronized (this.memoriaP) {
                this.memoriaP.alocarMemoria(processoAtual, processoAtual.getQuantidadeDeMemoriaRAM());
            }
        }
    }

    public void gerarProcesso() {
        /* Scanner para obter as informações dos novos processos */
        Scanner scanIN = new Scanner(System.in);
        
        int tempoCPU1, tempoES, tempoCPU2, qtdMemoria;

        System.out.println("---------------------------------");
        System.out.println("Interface de Criação de Processos");
        
        /* Loop para obtenção do primeiro tempo de cpu */
        while (true) {
            try {
                System.out.printf("Informe o primeiro tempo de CPU: ");
                tempoCPU1 = scanIN.nextInt();
                if (tempoCPU1 <= 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO1);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println("Erro: " + error);
                scanIN.nextLine();
            }
        }

        /* Loop para obtenção do tempo de ES */
        while (true) {
            try {
                System.out.printf("Informe o tempo de ES: ");
                tempoES = scanIN.nextInt();
                if (tempoES < 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO2);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println("Erro: " + error);
                scanIN.nextLine();
            }
        }

        /* Loop para obtenção do segundo tempo de cpu */
        while (true) {
            try {
                System.out.printf("Informe o segundo tempo de cpu: ");
                tempoCPU2 = scanIN.nextInt();
                if (tempoCPU2 < 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO2);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println("Erro: " + error);
                scanIN.nextLine();
            }
        }
        
        /* Loop para obtenção da quantidade de memória */
        while (true) {
            try {
                System.out.printf("Informe a quantidade de memória em MB do processo: ");
                qtdMemoria = scanIN.nextInt();
                if (qtdMemoria <= 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO1);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println("Erro: " + error);
                scanIN.nextLine();
            }
        }
        
        // TODO: retornar booleano ao alocar memória na mp? Contadição entre alocar memoria e criar processo

        Processo novoProcesso = new Processo(qtdMemoria);
        novoProcesso.setFaseAtual("Novo");
        novoProcesso.setTempoFaseCpu1(tempoCPU1);
        novoProcesso.setTempoDuracaoEntradaSaida(tempoES);
        novoProcesso.setTempoFaseCpu2(tempoCPU2);

        this.adicionarProcesso(novoProcesso);
    }

    @Override
    public void run() {
        while (true) { this.processarProcessos(); }
    }
}
