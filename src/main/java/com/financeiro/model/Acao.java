package com.financeiro.model;

/**
 * Representa uma Ação brasileira.
 * Ações são ativos NACIONAIS de RENDA VARIÁVEL.
 * O tipo da ação (ordinária, preferencial, unit) é definido pelo ticker.
 */
public class Acao extends Ativo implements AtivoNacional {

    public Acao(String ticker, String nome, double preco, boolean qualificado) {
        super(ticker, nome, preco, qualificado, 1.0); // Fator conversão 1.0 para nacionais
    }

    @Override
    public double getPrecoEmReais() {
        return getPreco(); // Já está em reais
    }

    public String getTipoAcao() {
        String ticker = getTicker();
        if (ticker.endsWith("3")) return "Ordinária";
        if (ticker.endsWith("4") || ticker.endsWith("5") || ticker.endsWith("6")) return "Preferencial";
        if (ticker.endsWith("11")) return "Unit";
        return "Desconhecido";
    }

    @Override
    public String toString() {
        return "Acao{" +
                "ticker='" + getTicker() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", preco=" + getPreco() +
                ", qualificado=" + isQualificado() +
                ", tipo='" + getTipoAcao() + '\'' +
                '}';
    }
}
