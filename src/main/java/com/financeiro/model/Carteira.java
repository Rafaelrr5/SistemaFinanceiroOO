package com.financeiro.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Carteira {
    private String nome;
    private LocalDate dataCriacao;
    private double saldo;
    private Map<Ativo, Integer> ativos;

    public Carteira(String nome) {
        this.nome = nome;
        this.dataCriacao = LocalDate.now();
        this.saldo = 0.0;
        this.ativos = new HashMap<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public Map<Ativo, Integer> getAtivos() {
        return ativos;
    }

    public void setAtivos(Map<Ativo, Integer> ativos) {
        this.ativos = ativos;
    }

    public void adicionarAtivo(Ativo ativo, int quantidade) {
        this.ativos.put(ativo, this.ativos.getOrDefault(ativo, 0) + quantidade);
    }
    
    public void removerAtivo(Ativo ativo, int quantidade) {
        if (this.ativos.containsKey(ativo)) {
            int qtdAtual = this.ativos.get(ativo);
            if (qtdAtual > quantidade) {
                this.ativos.put(ativo, qtdAtual - quantidade);
            } else {
                this.ativos.remove(ativo);
            }
        }
    }

    @Override
    public String toString() {
        return "Carteira{" +
                "nome='" + nome + '\'' +
                ", dataCriacao=" + dataCriacao +
                ", saldo=" + saldo +
                ", ativos=" + ativos +
                '}';
    }
}
