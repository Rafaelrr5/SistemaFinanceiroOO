package com.financeiro.model;

/**
 * Interface que representa ativos nacionais.
 * Ativos nacionais são negociados em Real (R$) e não requerem conversão.
 */
public interface AtivoNacional {
    /**
     * Retorna o preço do ativo em Reais (sem conversão necessária).
     */
    double getPrecoEmReais();
}
