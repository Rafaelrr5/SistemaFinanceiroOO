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

    /**
     * Verifica se o ativo é de renda fixa.
     * Sobrescrito nas subclasses quando necessário.
     */
    public boolean isRendaFixa() {
        return false; // Por padrão, ativos são de renda variável
    }

    public boolean isRendaVariavel() {
        return !isRendaFixa();
    }

    /**
     * Verifica se o ativo é nacional (negociado em Real).
     * Sobrescrito nas subclasses quando necessário.
     */
    public boolean isNacional() {
        return this.fatorConversao == 1.0; // Se fator é 1.0, é nacional
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
