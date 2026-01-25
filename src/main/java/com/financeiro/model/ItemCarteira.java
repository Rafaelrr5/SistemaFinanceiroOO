package com.financeiro.model;

public class ItemCarteira {
    private Ativo ativo;
    private double quantidade;
    private double precoMedioCompra;

    public ItemCarteira(Ativo ativo, double quantidade, double precoMedioCompra) {
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.precoMedioCompra = precoMedioCompra;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoMedioCompra() {
        return precoMedioCompra;
    }

    public void setPrecoMedioCompra(double precoMedioCompra) {
        this.precoMedioCompra = precoMedioCompra;
    }
    
    public double getValorTotalAtual() {
        return quantidade * ativo.getValorEmReais();
    }
    
    public double getValorTotalGasto() {
        return quantidade * precoMedioCompra;
    }
}
