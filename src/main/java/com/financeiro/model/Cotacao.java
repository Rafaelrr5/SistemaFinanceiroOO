package com.financeiro.model;

import java.time.LocalDate;

public class Cotacao {
    private LocalDate data;
    private double preco;
    private double variacao;

    public Cotacao(LocalDate data, double preco, double variacao) {
        this.data = data;
        this.preco = preco;
        this.variacao = variacao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public double getVariacao() {
        return variacao;
    }

    public void setVariacao(double variacao) {
        this.variacao = variacao;
    }

    @Override
    public String toString() {
        return "Cotacao{" +
                "data=" + data +
                ", preco=" + preco +
                ", variacao=" + variacao +
                '}';
    }
}
