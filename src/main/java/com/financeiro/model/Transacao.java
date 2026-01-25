package com.financeiro.model;

import java.time.LocalDateTime;

public class Transacao {
    private String id;
    private TipoTransacao tipo;
    private LocalDateTime data;
    private Ativo ativo;
    private double quantidade;
    private double preco;
    private double valorTotal;
    private String instituicao;

    public Transacao(String id, TipoTransacao tipo, LocalDateTime data, Ativo ativo, double quantidade, double preco, String instituicao) {
        this.id = id;
        this.tipo = tipo;
        this.data = data;
        this.ativo = ativo;
        this.quantidade = quantidade;
        this.preco = preco;
        this.valorTotal = quantidade * preco;
        this.instituicao = instituicao;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
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

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
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
                "id='" + id + '\'' +
                ", tipo=" + tipo +
                ", data=" + data +
                ", ativo=" + (ativo != null ? ativo.getNome() : "null") +
                ", instituicao='" + instituicao + '\'' +
                ", valorTotal=" + valorTotal +
                '}';
    }
}
