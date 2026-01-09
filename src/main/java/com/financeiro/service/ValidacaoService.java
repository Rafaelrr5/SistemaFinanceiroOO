package com.financeiro.service;

import com.financeiro.model.Ativo;
import com.financeiro.model.Carteira;

public class ValidacaoService {

    public void validarSaldoSuficiente(Carteira carteira, double valorCompra) throws Exception {
        if (carteira.getSaldo() < valorCompra) {
            throw new Exception("Saldo insuficiente para realizar a compra.");
        }
    }

    public void validarQuantidadeAtivo(Carteira carteira, Ativo ativo, int quantidadeVenda) throws Exception {
        if (!carteira.getAtivos().containsKey(ativo) || carteira.getAtivos().get(ativo) < quantidadeVenda) {
            throw new Exception("Quantidade de ativos insuficiente para realizar a venda.");
        }
    }

    public void validarQuantidadePositiva(int quantidade) throws Exception {
        if (quantidade <= 0) {
            throw new Exception("A quantidade deve ser maior que zero.");
        }
    }
}
