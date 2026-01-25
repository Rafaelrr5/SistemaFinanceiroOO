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

    @Override
    public String toString() {
        return "Institucional{" +
                "nome='" + getNome() + '\'' +
                ", cnpj='" + getIdentificador() + '\'' +
                ", razaoSocial='" + razaoSocial + '\'' +
                '}';
    }
}
