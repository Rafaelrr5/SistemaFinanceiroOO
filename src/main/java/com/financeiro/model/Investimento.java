package com.financeiro.model;

/**
 * Interface que define a capacidade de cadastrar investimentos.
 */
public interface Investimento {
    /**
     * Cadastra um investimento (compra) na carteira.
     * 
     * @param ativo      O ativo a ser adquirido
     * @param quantidade Quantidade do ativo
     * @param preco      Preço de compra
     * @return true se a operação foi bem sucedida, false caso contrário
     */
    boolean cadastrarInvestimento(Ativo ativo, double quantidade, double preco);

    /**
     * Verifica se o investidor pode movimentar determinado tipo de ativo.
     * 
     * @param ativo O ativo a ser verificado
     * @return true se o investidor pode movimentar o ativo
     */
    boolean podeMovimentar(Ativo ativo);
}
