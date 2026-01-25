package com.financeiro.model;

public class Stock extends Ativo {
    private String bolsaNegociacao;
    private String setor;

    public Stock(String ticker, String nome, double preco, String bolsaNegociacao, String setor, double fatorConversao) {
        super(ticker, nome, preco, false, fatorConversao); // Stock internacional
        this.bolsaNegociacao = bolsaNegociacao;
        this.setor = setor;
    }

    public String getBolsaNegociacao() {
        return bolsaNegociacao;
    }

    public void setBolsaNegociacao(String bolsaNegociacao) {
        this.bolsaNegociacao = bolsaNegociacao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    @Override
    public boolean isNacional() {
        return false; // Stocks são internacionais
    }

    @Override
    public String toString() {
        return "Stock{" +
                "ticker='" + getTicker() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", preco=" + getPreco() +
                ", bolsaNegociacao='" + bolsaNegociacao + '\'' +
                ", setor='" + setor + '\'' +
                '}';
    }
}
