class TrabalhoSO {
  public static void main(String[] args) {
    Fila<Descritor> filaProntos = new Fila<>();

    Fila<Descritor> filaAuxiliar = new Fila<>();

    CPU [] cpuArray = new CPU[4];
    Thread[] arrayThreadsCPU = new Thread[4];

    for (int i = 0; i < 4; i++) {
      cpuArray[i] = new CPU();
      ObservadorEstados.adicionarCPU(cpuArray[i]);
      Thread cpuThread = new Thread(cpuArray[i]);
      arrayThreadsCPU[i] = cpuThread;
      cpuThread.start();
    }

    MemoriaPrincipal memoriaPrincipal = MemoriaPrincipal.getInstance();

    Despachante despachante = new Despachante(filaProntos, filaAuxiliar, cpuArray);
    GeradorProcesso geradorProcesso = new GeradorProcesso(filaProntos, memoriaPrincipal);

    Thread threadDespachante = new Thread(despachante);
    Thread threadGerador = new Thread(geradorProcesso);

    threadDespachante.start();
    threadGerador.start();

    // geradorProcesso.gerarProcesso();
    MenuGerenciamento gerenciadorMenu = new MenuGerenciamento(geradorProcesso, memoriaPrincipal);

    gerenciadorMenu.iniciarMenu();
    
    // TODO: IMPLEMENTAR AS INTERRUPÇÕES
    threadDespachante.interrupt();
    threadGerador.interrupt();
    for (int i = 0; i < 4; i++) {
      arrayThreadsCPU[i].interrupt();
    }
  }
}
