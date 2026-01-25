package com.financeiro.service;

import com.financeiro.exception.RegraNegocioException;

/**
 * Classe utilitária para validação de documentos (CPF e CNPJ).
 */
public class DocumentoValidator {

    /**
     * Valida se o documento informado é um CPF ou CNPJ válido.
     * CPF: 11 dígitos
     * CNPJ: 14 dígitos
     * 
     * @param documento O documento a ser validado (apenas números)
     * @throws RegraNegocioException se o documento for inválido
     */
    public static void validarDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()) {
            throw new RegraNegocioException("Documento (CPF/CNPJ) não pode ser vazio.");
        }
        
        // Remove caracteres não numéricos
        String docLimpo = documento.replaceAll("[^0-9]", "");
        
        if (docLimpo.length() == 11) {
            validarCPF(docLimpo);
        } else if (docLimpo.length() == 14) {
            validarCNPJ(docLimpo);
        } else {
            throw new RegraNegocioException(
                "Documento inválido. CPF deve ter 11 dígitos e CNPJ deve ter 14 dígitos. " +
                "Informado: " + docLimpo.length() + " dígitos."
            );
        }
    }

    /**
     * Valida um CPF usando o algoritmo oficial.
     * @param cpf String com 11 dígitos numéricos
     */
    public static void validarCPF(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            throw new RegraNegocioException("CPF deve conter 11 dígitos numéricos.");
        }
        
        // Verifica se todos os dígitos são iguais (CPF inválido)
        if (cpf.matches("(\\d)\\1{10}")) {
            throw new RegraNegocioException("CPF inválido: todos os dígitos são iguais.");
        }
        
        try {
            // Calcula primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
            }
            int primeiroDigito = 11 - (soma % 11);
            if (primeiroDigito >= 10) primeiroDigito = 0;
            
            // Calcula segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
            }
            int segundoDigito = 11 - (soma % 11);
            if (segundoDigito >= 10) segundoDigito = 0;
            
            // Verifica os dígitos
            if (Character.getNumericValue(cpf.charAt(9)) != primeiroDigito ||
                Character.getNumericValue(cpf.charAt(10)) != segundoDigito) {
                throw new RegraNegocioException("CPF inválido: dígitos verificadores incorretos.");
            }
        } catch (NumberFormatException e) {
            throw new RegraNegocioException("CPF deve conter apenas números.");
        }
    }

    /**
     * Valida um CNPJ usando o algoritmo oficial.
     * @param cnpj String com 14 dígitos numéricos
     */
    public static void validarCNPJ(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) {
            throw new RegraNegocioException("CNPJ deve conter 14 dígitos numéricos.");
        }
        
        // Verifica se todos os dígitos são iguais (CNPJ inválido)
        if (cnpj.matches("(\\d)\\1{13}")) {
            throw new RegraNegocioException("CNPJ inválido: todos os dígitos são iguais.");
        }
        
        try {
            // Pesos para cálculo
            int[] pesosPrimeiroDigito = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            int[] pesosSegundoDigito = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
            
            // Calcula primeiro dígito verificador
            int soma = 0;
            for (int i = 0; i < 12; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * pesosPrimeiroDigito[i];
            }
            int primeiroDigito = soma % 11;
            primeiroDigito = primeiroDigito < 2 ? 0 : 11 - primeiroDigito;
            
            // Calcula segundo dígito verificador
            soma = 0;
            for (int i = 0; i < 13; i++) {
                soma += Character.getNumericValue(cnpj.charAt(i)) * pesosSegundoDigito[i];
            }
            int segundoDigito = soma % 11;
            segundoDigito = segundoDigito < 2 ? 0 : 11 - segundoDigito;
            
            // Verifica os dígitos
            if (Character.getNumericValue(cnpj.charAt(12)) != primeiroDigito ||
                Character.getNumericValue(cnpj.charAt(13)) != segundoDigito) {
                throw new RegraNegocioException("CNPJ inválido: dígitos verificadores incorretos.");
            }
        } catch (NumberFormatException e) {
            throw new RegraNegocioException("CNPJ deve conter apenas números.");
        }
    }

    /**
     * Formata um CPF para exibição (XXX.XXX.XXX-XX).
     */
    public static String formatarCPF(String cpf) {
        String limpo = cpf.replaceAll("[^0-9]", "");
        if (limpo.length() != 11) return cpf;
        return limpo.substring(0, 3) + "." + 
               limpo.substring(3, 6) + "." + 
               limpo.substring(6, 9) + "-" + 
               limpo.substring(9);
    }

    /**
     * Formata um CNPJ para exibição (XX.XXX.XXX/XXXX-XX).
     */
    public static String formatarCNPJ(String cnpj) {
        String limpo = cnpj.replaceAll("[^0-9]", "");
        if (limpo.length() != 14) return cnpj;
        return limpo.substring(0, 2) + "." + 
               limpo.substring(2, 5) + "." + 
               limpo.substring(5, 8) + "/" + 
               limpo.substring(8, 12) + "-" + 
               limpo.substring(12);
    }

    /**
     * Verifica se é um CPF (11 dígitos) ou CNPJ (14 dígitos).
     * @return "CPF", "CNPJ" ou "INVALIDO"
     */
    public static String identificarTipoDocumento(String documento) {
        if (documento == null) return "INVALIDO";
        String limpo = documento.replaceAll("[^0-9]", "");
        if (limpo.length() == 11) return "CPF";
        if (limpo.length() == 14) return "CNPJ";
        return "INVALIDO";
    }
}
