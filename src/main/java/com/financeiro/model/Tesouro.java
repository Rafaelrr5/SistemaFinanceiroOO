package com.financeiro.model;

/**
 * Representa um título do Tesouro Direto.
 * Tesouro são ativos NACIONAIS de RENDA FIXA.
 * Possuem: tipo de rendimento (Selic, Prefixado, IPCA+) e data de vencimento.
 */
public class Tesouro extends Ativo implements AtivoNacional {
    private String tipoRendimento;
    private String vencimento;

    public Tesouro(String ticker, String nome, double preco, String tipoRendimento, String vencimento) {
        super(ticker, nome, preco, false, 1.0);
        this.tipoRendimento = tipoRendimento;
        this.vencimento = vencimento;
    }

    @Override
    public double getPrecoEmReais() {
        return getPreco();
    }

    public String getTipoRendimento() {
        return tipoRendimento;
    }

    public void setTipoRendimento(String tipoRendimento) {
        this.tipoRendimento = tipoRendimento;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    @Override
    public boolean isRendaFixa() {
        return true;
    }

    @Override
    public boolean isNacional() {
        return true;
    }

    @Override
    public String toString() {
        return "Tesouro{" +
                "ticker='" + getTicker() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", preco=" + getPreco() +
                ", tipoRendimento='" + tipoRendimento + '\'' +
                ", vencimento='" + vencimento + '\'' +
                '}';
    }
}
