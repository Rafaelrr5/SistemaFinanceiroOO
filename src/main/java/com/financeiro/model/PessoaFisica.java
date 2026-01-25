package com.financeiro.model;

import java.time.LocalDate;

public class PessoaFisica extends Investidor {
    private PerfilInvestidor perfil;

    public PessoaFisica(String nome, String cpf, String telefone, String email, LocalDate dataNascimento, String enderecoCompleto, double patrimonio, PerfilInvestidor perfil) {
        super(nome, cpf, telefone, email, dataNascimento, enderecoCompleto, patrimonio);
        this.perfil = perfil;
    }

    public PerfilInvestidor getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilInvestidor perfil) {
        this.perfil = perfil;
    }

    /**
     * Verifica se a pessoa física pode movimentar determinado ativo.
     * Regras:
     * - Criptoativos: apenas perfil ARROJADO
     * - Stocks: apenas MODERADO ou ARROJADO
     * - Ativos qualificados: apenas se investidor é qualificado (patrimônio >= R$1M)
     */
    @Override
    public boolean podeMovimentar(Ativo ativo) {
        // Regra ativo qualificado
        if (ativo.isQualificado() && !this.isQualificado()) {
            return false;
        }
        
        // Regra criptoativos
        if (ativo instanceof Criptoativo) {
            return this.perfil == PerfilInvestidor.ARROJADO;
        }
        
        // Regra stocks
        if (ativo instanceof Stock) {
            return this.perfil == PerfilInvestidor.MODERADO || this.perfil == PerfilInvestidor.ARROJADO;
        }
        
        return true;
    }

    /**
     * Cadastra investimento (compra) na carteira.
     * Retorna true se bem sucedido, false se não permitido.
     */
    @Override
    public boolean cadastrarInvestimento(Ativo ativo, double quantidade, double preco) {
        if (!podeMovimentar(ativo)) {
            System.out.println("Movimentação não permitida para este perfil de investidor.");
            return false;
        }
        
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
        return "PessoaFisica{" +
                "nome='" + getNome() + '\'' +
                ", cpf='" + getIdentificador() + '\'' +
                ", perfil=" + perfil +
                ", qualificado=" + isQualificado() +
                '}';
    }
}
