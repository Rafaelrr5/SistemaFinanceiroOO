package com.financeiro.model;

public class Acao extends Ativo {
    private boolean qualificado;

    public Acao(String ticker, String nome, double preco, boolean qualificado) {
        super(ticker, nome, preco);
        this.qualificado = qualificado;
    }

    public boolean isQualificado() {
        return qualificado;
    }

    public void setQualificado(boolean qualificado) {
        this.qualificado = qualificado;
    }

    @Override
    public String toString() {
        return "Acao{" +
                "ticker='" + getTicker() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", preco=" + getPreco() +
                ", qualificado=" + qualificado +
                '}';
    }
}
