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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ativo ativo = (Ativo) o;
        return ticker.equals(ativo.ticker);
    }

    @Override
    public int hashCode() {
        return ticker.hashCode();
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
