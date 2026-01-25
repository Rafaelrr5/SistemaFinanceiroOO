package com.financeiro.model;

import java.time.LocalDate;

public class Institucional extends Investidor {
    private String razaoSocial;

    public Institucional(String nome, String cnpj, String telefone, String email, LocalDate dataNascimento, String enderecoCompleto, double patrimonio, String razaoSocial) {
        super(nome, cnpj, telefone, email, dataNascimento, enderecoCompleto, patrimonio);
        this.razaoSocial = razaoSocial;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    /**
     * Investidores institucionais podem movimentar qualquer ativo.
     */
    @Override
    public boolean podeMovimentar(Ativo ativo) {
        return true; // Institucional pode tudo
    }

    /**
     * Cadastra investimento (compra) na carteira.
     * Institucional não tem restrições de perfil.
     */
    @Override
    public boolean cadastrarInvestimento(Ativo ativo, double quantidade, double preco) {
        if (quantidade <= 0 || preco <= 0) {
            System.out.println("Quantidade e preço devem ser maiores que zero.");
            return false;
        }
        
        getCarteira().adicionarAtivo(ativo, quantidade, preco);
        System.out.println("Investimento cadastrado com sucesso!");
        return true;
    }

    @Override
    public String toString() {
        return "Institucional{" +
                "nome='" + getNome() + '\'' +
                ", cnpj='" + getIdentificador() + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                '}';
    }
}
