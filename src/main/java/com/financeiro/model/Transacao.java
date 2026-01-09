package com.financeiro.model;

import java.time.LocalDateTime;

public class Transacao {
    private TipoTransacao tipo;
    private LocalDateTime data;
    private Ativo ativo;
    private int quantidade;
    private double preco;
    private double valorTotal;

    public Transacao(TipoTransacao tipo, LocalDateTime data, Ativo ativo, int quantidade, double preco) {
        this.tipo = tipo;
        this.data = data;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.valorTotal = quantidade * preco;
    }

    public TipoTransacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoTransacao tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }

    public Ativo getAtivo() {
        return ativo;
    }

    public void setAtivo(Ativo ativo) {
        this.ativo = ativo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
        this.valorTotal = this.quantidade * this.preco;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
        this.valorTotal = this.quantidade * this.preco;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "tipo=" + tipo +
                ", data=" + data +
                ", ativo=" + (ativo != null ? ativo.getNome() : "null") +
                ", quantidade=" + quantidade +
                ", preco=" + preco +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
