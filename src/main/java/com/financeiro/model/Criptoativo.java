package com.financeiro.model;

/**
 * Representa uma Criptomoeda.
 * Criptoativos são ativos INTERNACIONAIS de RENDA VARIÁVEL.
 * Possuem: algoritmo de consenso (PoW, PoS) e quantidade máxima em circulação.
 */
public class Criptoativo extends Ativo implements AtivoInternacional {
    private String algoritmoConsenso;
    private double quantidadeMaxima;
    private String moedaOriginal;

    public Criptoativo(String ticker, String nome, double preco, String algoritmoConsenso, double quantidadeMaxima, double fatorConversao) {
        super(ticker, nome, preco, false, fatorConversao); // Cripto internacional
        this.algoritmoConsenso = algoritmoConsenso;
        this.quantidadeMaxima = quantidadeMaxima;
        this.moedaOriginal = "USD"; // Padrão USD
    }

    /**
     * Método OBRIGATÓRIO conforme especificação:
     * "Ativos internacionais devem obrigatoriamente ter um método para converter a moeda do ativo em reais."
     */
    @Override
    public double converterParaReais() {
        return getPreco() * getFatorConversao();
    }

    @Override
    public String getMoedaOriginal() {
        return moedaOriginal;
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
    public boolean isNacional() {
        return false; // Criptoativos são internacionais
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
