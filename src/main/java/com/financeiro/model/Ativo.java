package com.financeiro.model;

public abstract class Ativo {
    private String ticker;
    private String nome;
    private double preco;

    public Ativo(String ticker, String nome, double preco) {
        this.ticker = ticker;
        this.nome = nome;
        this.preco = preco;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Ativo{" +
                "ticker='" + ticker + '\'' +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                '}';
    }
}
