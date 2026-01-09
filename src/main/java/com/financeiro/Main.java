package com.financeiro;

import com.financeiro.model.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Sistema Financeiro OO - Teste de Classes");

        // Test Acao
        Acao acao = new Acao("PETR4", "Petrobras", 34.50, true);
        System.out.println(acao);

        // Test FII
        FII fii = new FII("KNIP11", "Kinea Indices de Precos", 100.50, "Papel", 1.10, 0.01);
        System.out.println(fii);

        // Test Stock
        Stock stock = new Stock("AAPL", "Apple Inc.", 175.00, "NASDAQ", "Tecnologia");
        System.out.println(stock);

        // Test Tesouro
        Tesouro tesouro = new Tesouro("LFT2024", "Tesouro Selic 2024", 12000.00, "Selic", "01/03/2024");
        System.out.println(tesouro);

        // Test Criptoativo
        Criptoativo cripto = new Criptoativo("BTC", "Bitcoin", 45000.00, "Proof of Work", 21000000);
        System.out.println(cripto);
    }
}
