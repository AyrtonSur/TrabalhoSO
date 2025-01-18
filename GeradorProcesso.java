import java.util.ArrayDeque;
import java.util.Deque;
import java.util.InputMismatchException;
import java.util.regex.Pattern;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class GeradorProcesso implements Runnable {
    private static final String MENSAGEMERRO1 = "Erro: Informe um valor positivo maior que zero.";
    private static final String MENSAGEMERRO2 = "Erro: Informe um valor positivo maior ou igual a zero.";
    private static final String MENSAGEMERRO3 = "Erro: Espaço insuficiente disponível para alocar o processo.";
    private static final String MENSAGEMERRO4 = "Erro: Apenas números inteiros positivos são aceitos.";
    private Deque<Processo> filaProcessos;
    private Fila<Descritor> filaProntos;
    private MemoriaPrincipal memoriaP;
    private Processo processoAtual = null;

    public GeradorProcesso(Fila<Descritor> filaProntos, MemoriaPrincipal memoriaP) {
        this.filaProcessos = new ArrayDeque<Processo>();
        this.filaProntos = filaProntos;
        this.memoriaP = memoriaP;
    }

    private void adicionarProcesso(Processo p) {
        synchronized(this.filaProcessos) {
            this.filaProcessos.addLast(p);
        }
    }

    private void processarProcessos() {
        synchronized (this.filaProcessos) {
            if (this.filaProcessos.peekFirst() != null) {
                this.processoAtual = this.filaProcessos.pollFirst();
            }
        }
        if (this.processoAtual != null) {
            synchronized (this.memoriaP) {
                this.memoriaP.alocarMemoria(this.processoAtual, processoAtual.getQuantidadeDeMemoriaRAM());
            }
            synchronized (this.filaProntos) {
                this.filaProntos.adicionar(this.processoAtual.getDescritor());
            }
            this.processoAtual = null;
        }
    }

    public void gerarProcesso() {
        /* Scanner para obter as informações dos novos processos */
        Scanner scanIN = new Scanner(System.in);
        
        int tempoCPU1, tempoES, tempoCPU2, qtdMemoria;

        System.out.println("|---------------------------------|");
        System.out.println(" Interface de Criação de Processos");
        System.out.println("  (digite \'sair\' para cancelar')\n");
        
        String respostaUsuario;
        /* Loop para obtenção do primeiro tempo de cpu */
        while (true) {
            try {
                System.out.printf("Informe o primeiro tempo de CPU: ");
                respostaUsuario = scanIN.nextLine().strip().toLowerCase();
                if (respostaUsuario.equals("sair")) return;
                
                if (!Pattern.matches("^\\d+$", respostaUsuario))
                    throw new NumberFormatException(GeradorProcesso.MENSAGEMERRO4);

                tempoCPU1 = Integer.parseInt(respostaUsuario);
                if (tempoCPU1 <= 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO1);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println(error.getMessage());
                scanIN.nextLine();
            } catch(NumberFormatException error) {
                System.out.println(error.getMessage());
            }
        }

        /* Loop para obtenção do tempo de ES */
        while (true) {
            try {
                System.out.printf("Informe o tempo de ES: ");
                respostaUsuario = scanIN.nextLine().strip().toLowerCase();

                if (respostaUsuario.equals("sair")) return;
                if (!Pattern.matches("^\\d+$", respostaUsuario))
                    throw new NumberFormatException(GeradorProcesso.MENSAGEMERRO4);
                
                tempoES = Integer.parseInt(respostaUsuario);
                if (tempoES < 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO2);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println(error.getMessage());
                scanIN.nextLine();
            } catch(NumberFormatException error) {
                System.out.println(error.getMessage());
            }
        }

        /* Loop para obtenção do segundo tempo de cpu */
        while (true) {
            try {
                System.out.printf("Informe o segundo tempo de CPU: ");
                respostaUsuario = scanIN.nextLine().strip().toLowerCase();
                if (respostaUsuario.equals("sair")) return;
                if (!Pattern.matches("^\\d+$", respostaUsuario))
                    throw new NumberFormatException(GeradorProcesso.MENSAGEMERRO4);
                
                tempoCPU2 = Integer.parseInt(respostaUsuario);
                if (tempoCPU2 < 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO2);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println(error.getMessage());
                scanIN.nextLine();
            } catch(NumberFormatException error) {
                System.out.println(error.getMessage());
            }
        }
        
        /* Loop para obtenção da quantidade de memória */
        while (true) {
            try {
                System.out.printf("Informe a quantidade de memória em MB do processo: ");
                respostaUsuario = scanIN.nextLine().strip().toLowerCase();
                if (respostaUsuario.equals("sair")) return;
                if (!Pattern.matches("^\\d+$", respostaUsuario))
                    throw new NumberFormatException(GeradorProcesso.MENSAGEMERRO4);
                qtdMemoria = Integer.parseInt(respostaUsuario);
                if (qtdMemoria <= 0) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO1);
                }
                if (!this.memoriaP.possivelAlocacao(qtdMemoria)) {
                    throw new InputMismatchException(GeradorProcesso.MENSAGEMERRO3);
                }
                break;
            } catch(InputMismatchException error) {
                System.out.println(error.getMessage());
                scanIN.nextLine();
            } catch(NumberFormatException error) {
                System.out.println(error.getMessage());
            }
        }
        
        // TODO: retornar booleano ao alocar memória na mp? Contadição entre alocar memoria e criar processo

        Processo novoProcesso = new Processo(qtdMemoria);
        novoProcesso.setFaseAtual("FaseCPU1");
        novoProcesso.setTempoFaseCpu1(tempoCPU1);
        novoProcesso.setTempoDuracaoEntradaSaida(tempoES);
        novoProcesso.setTempoFaseCpu2(tempoCPU2);
        novoProcesso.setTransicaoDeEstados("Pronto");

        this.adicionarProcesso(novoProcesso);

    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            this.processarProcessos();
        }
    }
}
