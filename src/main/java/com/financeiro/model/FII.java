package com.financeiro.model;

public class FII extends Ativo {
    private String setor;
    private double ultimoDividendo;
    private double taxaAdministracao;

    public FII(String ticker, String nome, double preco, String setor, double ultimoDividendo, double taxaAdministracao) {
        super(ticker, nome, preco);
        this.setor = setor;
        this.ultimoDividendo = ultimoDividendo;
        this.taxaAdministracao = taxaAdministracao;
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
