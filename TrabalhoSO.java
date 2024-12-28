class TrabalhoSO {
    public static void main(String[] args) {
        Fila<Descritor> filaProntos = new Fila<>();

        Fila<Descritor> filaAuxiliar = new Fila<>();

        CPU[] cpuArray = new CPU[4];
        for (int i = 0; i < 4; i++) {
            cpuArray[i] = new CPU();
        }

        MemoriaPrincipal memoriaPrincipal = MemoriaPrincipal.getInstance();

        Despachante despachante = new Despachante(filaProntos, filaAuxiliar, cpuArray);

        GeradorProcesso geradorProcesso = new GeradorProcesso(filaProntos, memoriaPrincipal);

        Thread cpu1 = new Thread(cpuArray[0]);
        Thread cpu2 = new Thread(cpuArray[1]);
        Thread cpu3 = new Thread(cpuArray[2]);
        Thread cpu4 = new Thread(cpuArray[3]);

        Thread threadDespachante = new Thread(despachante);

        Thread threadGerador = new Thread(geradorProcesso);

        cpu1.start();
        cpu2.start();
        cpu3.start();
        cpu4.start();

        threadDespachante.start();

        threadGerador.start();

        geradorProcesso.gerarProcesso();

        for (Descritor d : filaProntos) {
            System.out.println(d.toString());
        }
    }
}