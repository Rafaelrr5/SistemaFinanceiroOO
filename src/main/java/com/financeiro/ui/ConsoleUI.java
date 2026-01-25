package com.financeiro.ui;

public class ConsoleUI {
    public static void limparConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("\n".repeat(50));
        }
    }

    public static void exibirCabecalho(String titulo) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(titulo);
        System.out.println("=".repeat(60));
    }

    public static void exibirMensagem(String mensagem) {
        System.out.println("\n" + mensagem);
    }

    public static void exibirErro(String erro) {
        System.out.println("\n[ERRO] " + erro);
    }

    public static void exibirSucesso(String sucesso) {
        System.out.println("\n[SUCESSO] " + sucesso);
    }

    public static void aguardarEnter() {
        System.out.println("\nPressione Enter para continuar...");
        try {
            System.in.read();
        } catch (Exception e) {
        }
    }

    public static void exibirSeparador() {
        System.out.println("-".repeat(60));
    }
}