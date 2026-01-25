package com.financeiro.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Carteira {
    private String nome;
    private LocalDate dataCriacao;
    private double saldo;
    private Map<Ativo, ItemCarteira> ativos;

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

    public Map<Ativo, ItemCarteira> getAtivos() {
        return ativos;
    }

    public void setAtivos(Map<Ativo, ItemCarteira> ativos) {
        this.ativos = ativos;
    }

    public void adicionarAtivo(Ativo ativo, double quantidade, double precoCompra) {
        if (ativos.containsKey(ativo)) {
            ItemCarteira item = ativos.get(ativo);
            double qtdAntiga = item.getQuantidade();
            double precoMedioAntigo = item.getPrecoMedioCompra();
            
            // Novo Preço Médio = ((QtdAntiga * PrecoMedioAntigo) + (QtdNova * PrecoCompra)) / (QtdTotal)
            double novoTotalQuantidade = qtdAntiga + quantidade;
            double novoPrecoMedio = ((qtdAntiga * precoMedioAntigo) + (quantidade * precoCompra)) / novoTotalQuantidade;
            
            item.setQuantidade(novoTotalQuantidade);
            item.setPrecoMedioCompra(novoPrecoMedio);
        } else {
            ativos.put(ativo, new ItemCarteira(ativo, quantidade, precoCompra));
        }
    }
    
    public void removerAtivo(Ativo ativo, double quantidade) {
        if (this.ativos.containsKey(ativo)) {
            ItemCarteira item = this.ativos.get(ativo);
            double qtdAtual = item.getQuantidade();
            if (qtdAtual > quantidade) {
                item.setQuantidade(qtdAtual - quantidade);
            } else {
                this.ativos.remove(ativo);
            }
        }
    }

    public double getValorTotalCarteira() {
        return saldo + ativos.values().stream().mapToDouble(ItemCarteira::getValorTotalAtual).sum();
    }

    @Override
    public String toString() {
        return "Carteira{" +
                "nome='" + nome + '\'' +
                ", saldo=" + saldo +
                ", qtdAtivos=" + ativos.size() +
                '}';
    }
}
