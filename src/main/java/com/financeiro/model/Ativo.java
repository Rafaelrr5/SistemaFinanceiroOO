package com.financeiro.model;

public abstract class Ativo {
    private String ticker;
    private String nome;
    private double preco;
    private boolean qualificado;
    private double fatorConversao;

    public Ativo(String ticker, String nome, double preco, boolean qualificado, double fatorConversao) {
        this.ticker = ticker;
        this.nome = nome;
        this.preco = preco;
        this.qualificado = qualificado;
        this.fatorConversao = fatorConversao;
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

    public boolean isQualificado() {
        return qualificado;
    }

    public void setQualificado(boolean qualificado) {
        this.qualificado = qualificado;
    }

    public double getFatorConversao() {
        return fatorConversao;
    }

    public void setFatorConversao(double fatorConversao) {
        this.fatorConversao = fatorConversao;
    }

    public double getValorEmReais() {
        return this.preco * this.fatorConversao;
    }

    public boolean isRendaFixa() {
        return this instanceof Tesouro;
    }

    public boolean isRendaVariavel() {
        return !isRendaFixa();
    }

    public boolean isNacional() {
        // Stock e Criptoativo são internacionais
        // Acao, FII, Tesouro são nacionais
        return !(this instanceof Stock) && !(this instanceof Criptoativo);
    }

    public boolean isInternacional() {
        return !isNacional();
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
