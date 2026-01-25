package com.financeiro.exception;

/**
 * Exceção personalizada para regras de negócio do sistema financeiro.
 * Estende RuntimeException para evitar try-catch excessivo em operações comuns,
 * permitindo um código mais limpo enquanto ainda mantém a capacidade de
 * tratar exceções quando necessário.
 */
public class RegraNegocioException extends RuntimeException {
    
    public RegraNegocioException(String message) {
        super(message);
    }
    
    public RegraNegocioException(String message, Throwable cause) {
        super(message, cause);
    }
}
