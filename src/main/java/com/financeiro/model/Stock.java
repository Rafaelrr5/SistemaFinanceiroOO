package com.financeiro.model;

/**
 * Representa uma Stock (ação internacional).
 * Stocks são ativos INTERNACIONAIS de RENDA VARIÁVEL.
 * Possuem: bolsa de negociação (NYSE, NASDAQ) e setor da empresa.
 */
public class Stock extends Ativo implements AtivoInternacional {
    private String bolsaNegociacao;
    private String setor;
    private String moedaOriginal;

    public Stock(String ticker, String nome, double preco, String bolsaNegociacao, String setor, double fatorConversao) {
        super(ticker, nome, preco, false, fatorConversao); // Stock internacional
        this.bolsaNegociacao = bolsaNegociacao;
        this.setor = setor;
        this.moedaOriginal = "USD"; // Padrão USD
    }

    public Stock(String ticker, String nome, double preco, String bolsaNegociacao, String setor, double fatorConversao, String moeda) {
        super(ticker, nome, preco, false, fatorConversao); // Stock internacional
        this.bolsaNegociacao = bolsaNegociacao;
        this.setor = setor;
        this.moedaOriginal = moeda;
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
