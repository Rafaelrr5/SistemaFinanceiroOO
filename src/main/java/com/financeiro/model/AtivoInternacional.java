package com.financeiro.model;

/**
 * Interface que representa ativos internacionais.
 * Ativos internacionais têm preço em moeda estrangeira e precisam de conversão.
 */
public interface AtivoInternacional {
    /**
     * Retorna o fator de conversão da moeda estrangeira para Real.
     */
    double getFatorConversao();

    /**
     * Converte o preço do ativo para Reais.
     */
    double converterParaReais();

    /**
     * Retorna o símbolo da moeda original do ativo (ex: USD, EUR).
     */
    String getMoedaOriginal();
}
