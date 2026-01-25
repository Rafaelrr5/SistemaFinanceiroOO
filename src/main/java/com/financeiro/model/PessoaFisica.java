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

    @Override
    public String toString() {
        return "PessoaFisica{" +
                "nome='" + getNome() + '\'' +
                ", cpf='" + getIdentificador() + '\'' +
                ", perfil=" + perfil +
                '}';
    }
}
