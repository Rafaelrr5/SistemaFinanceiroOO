package com.financeiro.service;

import com.financeiro.exception.RegraNegocioException;
import com.financeiro.model.*;

public class ValidacaoService {

    public void validarSaldoSuficiente(Carteira carteira, double valorCompra) throws RegraNegocioException {
        if (carteira.getSaldo() < valorCompra) {
            throw new RegraNegocioException("Saldo insuficiente para realizar a compra.");
        }
    }

    public void validarQuantidadeAtivo(Carteira carteira, Ativo ativo, double quantidadeVenda) throws RegraNegocioException {
        ItemCarteira item = carteira.getAtivos().get(ativo);
        if (item == null || item.getQuantidade() < quantidadeVenda) {
            throw new RegraNegocioException("Quantidade de ativos insuficiente para realizar a venda.");
        }
    }

    public void validarQuantidadePositiva(double quantidade) throws RegraNegocioException {
        if (quantidade <= 0) {
            throw new RegraNegocioException("A quantidade deve ser maior que zero.");
        }
    }

    public void validarPrecoPositivo(double preco) throws RegraNegocioException {
        if (preco <= 0) {
            throw new RegraNegocioException("O preço deve ser maior que zero.");
        }
    }

    public void validarPermissaoInvestimento(Investidor investidor, Ativo ativo) throws RegraNegocioException {
        if (investidor instanceof Institucional) {
            return; // Institucional pode tudo
        }

        if (investidor instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) investidor;

            // Regra Ativo Qualificado
            if (ativo.isQualificado() && !pf.isQualificado()) {
                throw new RegraNegocioException("Ativo restrito a investidores qualificados (Patrimônio > R$ 1MM).");
            }

            // Regra Criptoativos
            if (ativo instanceof Criptoativo) {
                if (pf.getPerfil() != PerfilInvestidor.ARROJADO) {
                    throw new RegraNegocioException("Apenas investidores de perfil ARROJADO podem operar Criptoativos.");
                }
            }

            // Regra BO (Stocks)
            if (ativo instanceof Stock) {
                if (pf.getPerfil() == PerfilInvestidor.CONSERVADOR) {
                    throw new RegraNegocioException("Investidores de perfil CONSERVADOR não podem operar Stocks.");
                }
            }
        }
    }
}
