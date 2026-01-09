package com.financeiro.model;

public class Tesouro extends Ativo {
    private String tipoRendimento;
    private String vencimento;

    public Tesouro(String ticker, String nome, double preco, String tipoRendimento, String vencimento) {
        super(ticker, nome, preco);
        this.tipoRendimento = tipoRendimento;
        this.vencimento = vencimento;
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
