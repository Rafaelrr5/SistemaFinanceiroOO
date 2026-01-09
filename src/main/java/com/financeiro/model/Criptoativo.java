package com.financeiro.model;

public class Criptoativo extends Ativo {
    private String algoritmoConsenso;
    private double quantidadeMaxima;

    public Criptoativo(String ticker, String nome, double preco, String algoritmoConsenso, double quantidadeMaxima) {
        super(ticker, nome, preco);
        this.algoritmoConsenso = algoritmoConsenso;
        this.quantidadeMaxima = quantidadeMaxima;
    }

    public String getAlgoritmoConsenso() {
        return algoritmoConsenso;
    }

    public void setAlgoritmoConsenso(String algoritmoConsenso) {
        this.algoritmoConsenso = algoritmoConsenso;
    }

    public double getQuantidadeMaxima() {
        return quantidadeMaxima;
    }

    public void setQuantidadeMaxima(double quantidadeMaxima) {
        this.quantidadeMaxima = quantidadeMaxima;
    }

    @Override
    public String toString() {
        return "Criptoativo{" +
                "ticker='" + getTicker() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", preco=" + getPreco() +
                ", algoritmoConsenso='" + algoritmoConsenso + '\'' +
                ", quantidadeMaxima=" + quantidadeMaxima +
                '}';
    }
}
