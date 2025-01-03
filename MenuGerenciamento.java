import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.lang.Thread;
import java.lang.Runnable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class MenuGerenciamento {
    private static final Object LOCKER = new Object();               // Objeto genérico atuando unicamente como artifício de sincronização.
    private static Boolean loopExibicaoEstados;
    private static Boolean loopExibicaoMemoria;
    private final String ALTERNATIVASVALIDAS = "1234";
    private GeradorProcesso geradorProcessos;
    private MemoriaPrincipal memoriaPrinc;
    private Boolean doLoop = true;
    
    public MenuGerenciamento(GeradorProcesso geradorProcessos, MemoriaPrincipal memoriaPrinc) {
        this.geradorProcessos = geradorProcessos;
        this.memoriaPrinc = memoriaPrinc;
    }

    private void limparTerminal() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private void exibirInterfaceMenu() {
        System.out.println("\t".repeat(2) + "|| MENU ||");
        System.out.println("|" + "-".repeat(40) + "|");
        System.out.println("  1 - Criar Processo");
        System.out.println("  2 - Visualizar Estado de Execução");
        System.out.println("  3 - Visualizar Memória Principal");
        System.out.println("  4 - Sair");
        System.out.println("|" + "-".repeat(40) + "|");

        return;
    }

    private synchronized void exibicaoExtadoExecucao() {
        MenuGerenciamento.loopExibicaoEstados = true;
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Scanner(System.in).nextLine();
                synchronized (MenuGerenciamento.LOCKER) { MenuGerenciamento.loopExibicaoEstados = false; }
            }
        }).start();

        while (true) {
            synchronized (MenuGerenciamento.LOCKER) {
                if (!MenuGerenciamento.loopExibicaoEstados) break;
            }
            this.limparTerminal();
            System.out.println("|\t\tEstado de Execução do Sistema:\t\t|\n");
            ObservadorEstados.exibirEstadosCPUS();

            try { TimeUnit.SECONDS.sleep(1); // Pequeno atraso para atualizar cada frame no terminal.
            } catch (InterruptedException e) {}
        }
    }

    public synchronized void visualizarMemoria() {
        MenuGerenciamento.loopExibicaoMemoria = true;
    
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Scanner(System.in).nextLine();
                synchronized (MenuGerenciamento.LOCKER) { MenuGerenciamento.loopExibicaoMemoria = false; }
            }
        }).start();

        while (true) {
            synchronized (MenuGerenciamento.LOCKER) {
                if (!MenuGerenciamento.loopExibicaoMemoria) break;
            }

            this.limparTerminal();
            System.out.println("|\t\tEstado da Memória Principal do Sistema:\t\t|\n");

            long memoriaTotalOcupada = 0, memoriaOcupadaProcesso = 0;
            for (String idProcessos : this.memoriaPrinc.getMenorEMaior().keySet()) {
                int[] intervaloAlocacao = this.memoriaPrinc.getMenorEMaior().get(idProcessos);
                memoriaOcupadaProcesso = intervaloAlocacao[1] - intervaloAlocacao[0];
                memoriaTotalOcupada += memoriaOcupadaProcesso;
                System.out.println("- Processo < " + idProcessos + " > ocupa " + memoriaOcupadaProcesso + " bytes em memória;");
            }

            
            System.out.println("\n      \\ --- Quantidade de Memória Total Ocupada: " + new BigDecimal((double)memoriaTotalOcupada / this.memoriaPrinc.getMemoriaTotal() * 100).setScale(2, RoundingMode.HALF_DOWN) + "% --- /");

            try { TimeUnit.SECONDS.sleep(1); // Pequeno atraso para atualizar cada frame no terminal.
            } catch (InterruptedException e) {}
        }
    }

    public synchronized void iniciarMenu() {
        Scanner scanIN = new Scanner(System.in);
        String respostaUsuario = "";

        while (this.doLoop) {
            this.limparTerminal();
            this.exibirInterfaceMenu();
            
            System.out.printf("\n\n>>> ");
            respostaUsuario = scanIN.nextLine().strip().toLowerCase();

            // Validação da Resposta do Usuário:
            if (!Pattern.matches("^(" + String.join("|",
                this.ALTERNATIVASVALIDAS.split("")) + ")$",
                respostaUsuario)) {
                    System.out.println("\nErro: Alternativa Inválida. Por favor, "
                    + "selecione um número correspondente às alternativas disponíveis no menu principal.");
                    System.out.println("Pressione a tecla \'Enter\' para prosseguir...");
                    
                    scanIN.nextLine();
                    continue;
            }

            this.limparTerminal();
            switch (respostaUsuario) {
                case "1":
                    this.geradorProcessos.gerarProcesso();
                    break;
                case "2":
                    this.exibicaoExtadoExecucao();
                    break;
                case "3":
                this.visualizarMemoria();
                    break;
                case "4":
                    this.doLoop = false;
                    break;
            }
        }

        return;
    }

}
