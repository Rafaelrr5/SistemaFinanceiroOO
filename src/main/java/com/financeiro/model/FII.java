package com.financeiro.model;

/**
 * Representa um Fundo de Investimento Imobiliário (FII).
 * FIIs são ativos NACIONAIS de RENDA VARIÁVEL.
 * Possuem: segmento, valor do último dividendo e taxa de administração.
 */
public class FII extends Ativo implements AtivoNacional {
    private String setor;
    private double ultimoDividendo;
    private double taxaAdministracao;

    public FII(String ticker, String nome, double preco, String setor, double ultimoDividendo,
            double taxaAdministracao) {
        super(ticker, nome, preco, false, 1.0);
        this.setor = setor;
        this.ultimoDividendo = ultimoDividendo;
        this.taxaAdministracao = taxaAdministracao;
    }

    public FII(String ticker, String nome, double preco, String setor, double ultimoDividendo, double taxaAdministracao,
            boolean qualificado) {
        super(ticker, nome, preco, qualificado, 1.0);
        this.setor = setor;
        this.ultimoDividendo = ultimoDividendo;
        this.taxaAdministracao = taxaAdministracao;
    }

    @Override
    public double getPrecoEmReais() {
        return getPreco();
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public double getUltimoDividendo() {
        return ultimoDividendo;
    }

    public void setUltimoDividendo(double ultimoDividendo) {
        this.ultimoDividendo = ultimoDividendo;
    }

    public double getTaxaAdministracao() {
        return taxaAdministracao;
    }

    public String getTaxaAdministracaoFormatada() {
        return taxaAdministracao + "%";
    }

    public void setTaxaAdministracao(double taxaAdministracao) {
        this.taxaAdministracao = taxaAdministracao;
    }

    @Override
    public String toString() {
        return "FII{" +
                "ticker='" + getTicker() + '\'' +
                ", nome='" + getNome() + '\'' +
                ", preco=" + getPreco() +
                ", setor='" + setor + '\'' +
                ", ultimoDividendo=" + ultimoDividendo +
                ", taxaAdministracao=" + taxaAdministracao +
                '}';
    }
}
