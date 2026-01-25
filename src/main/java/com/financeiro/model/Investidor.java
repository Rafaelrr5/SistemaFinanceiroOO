package com.financeiro.model;

import java.time.LocalDate;

public abstract class Investidor {
    private String nome;
    private String identificador; // CPF ou CNPJ
    private String telefone;
    private String email;
    private LocalDate dataNascimento;
    private String enderecoCompleto; // Simplificado
    private double patrimonio;
    private Carteira carteira;

    public Investidor(String nome, String identificador, String telefone, String email, LocalDate dataNascimento, String enderecoCompleto, double patrimonio) {
        this.nome = nome;
        this.identificador = identificador;
        this.telefone = telefone;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.enderecoCompleto = enderecoCompleto;
        this.patrimonio = patrimonio;
        this.carteira = new Carteira(nome + "'s Carteira");
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEnderecoCompleto() {
        return enderecoCompleto;
    }

    public void setEnderecoCompleto(String enderecoCompleto) {
        this.enderecoCompleto = enderecoCompleto;
    }

    public double getPatrimonio() {
        return patrimonio;
    }

    public void setPatrimonio(double patrimonio) {
        this.patrimonio = patrimonio;
    }

    public Carteira getCarteira() {
        return carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public boolean isQualificado() {
        return this.patrimonio >= 1000000.00;
    }

    @Override
    public String toString() {
        return "Investidor{" +
                "nome='" + nome + '\'' +
                ", identificador='" + identificador + '\'' +
                ", telefone='" + telefone + '\'' +
                ", patrimonio=" + patrimonio +
                '}';
    }
}
