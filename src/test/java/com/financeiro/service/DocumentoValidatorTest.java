package com.financeiro.service;

import com.financeiro.exception.RegraNegocioException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para DocumentoValidator.
 */
@DisplayName("DocumentoValidator")
class DocumentoValidatorTest {

    @Nested
    @DisplayName("Validação de CPF")
    class ValidacaoCPF {

        @Test
        @DisplayName("Deve aceitar CPF válido")
        void deveAceitarCpfValido() {
            // CPFs válidos para teste
            assertDoesNotThrow(() -> DocumentoValidator.validarCPF("52998224725"));
            assertDoesNotThrow(() -> DocumentoValidator.validarCPF("11144477735"));
        }

        @Test
        @DisplayName("Deve rejeitar CPF com dígitos iguais")
        void deveRejeitarCpfComDigitosIguais() {
            RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCPF("11111111111"));
            assertTrue(ex.getMessage().contains("dígitos são iguais"));
        }

        @Test
        @DisplayName("Deve rejeitar CPF com quantidade errada de dígitos")
        void deveRejeitarCpfComQuantidadeErrada() {
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCPF("1234567890")); // 10 dígitos
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCPF("123456789012")); // 12 dígitos
        }

        @Test
        @DisplayName("Deve rejeitar CPF com dígitos verificadores incorretos")
        void deveRejeitarCpfComDigitosIncorretos() {
            RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCPF("52998224726")); // Último dígito errado
            assertTrue(ex.getMessage().contains("dígitos verificadores incorretos"));
        }

        @Test
        @DisplayName("Deve rejeitar CPF nulo")
        void deveRejeitarCpfNulo() {
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCPF(null));
        }
    }

    @Nested
    @DisplayName("Validação de CNPJ")
    class ValidacaoCNPJ {

        @Test
        @DisplayName("Deve aceitar CNPJ válido")
        void deveAceitarCnpjValido() {
            // CNPJs válidos para teste
            assertDoesNotThrow(() -> DocumentoValidator.validarCNPJ("11222333000181"));
            assertDoesNotThrow(() -> DocumentoValidator.validarCNPJ("11444777000161"));
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com dígitos iguais")
        void deveRejeitarCnpjComDigitosIguais() {
            RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCNPJ("11111111111111"));
            assertTrue(ex.getMessage().contains("dígitos são iguais"));
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com quantidade errada de dígitos")
        void deveRejeitarCnpjComQuantidadeErrada() {
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCNPJ("1234567890123")); // 13 dígitos
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCNPJ("123456789012345")); // 15 dígitos
        }

        @Test
        @DisplayName("Deve rejeitar CNPJ com dígitos verificadores incorretos")
        void deveRejeitarCnpjComDigitosIncorretos() {
            RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarCNPJ("11222333000182")); // Último dígito errado
            assertTrue(ex.getMessage().contains("dígitos verificadores incorretos"));
        }
    }

    @Nested
    @DisplayName("Validação genérica de documento")
    class ValidacaoDocumento {

        @Test
        @DisplayName("Deve identificar e validar CPF automaticamente")
        void deveIdentificarEValidarCPF() {
            assertDoesNotThrow(() -> DocumentoValidator.validarDocumento("529.982.247-25"));
            assertDoesNotThrow(() -> DocumentoValidator.validarDocumento("52998224725"));
        }

        @Test
        @DisplayName("Deve identificar e validar CNPJ automaticamente")
        void deveIdentificarEValidarCNPJ() {
            assertDoesNotThrow(() -> DocumentoValidator.validarDocumento("11.222.333/0001-81"));
            assertDoesNotThrow(() -> DocumentoValidator.validarDocumento("11222333000181"));
        }

        @Test
        @DisplayName("Deve rejeitar documento vazio")
        void deveRejeitarDocumentoVazio() {
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarDocumento(""));
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarDocumento("   "));
            assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarDocumento(null));
        }

        @Test
        @DisplayName("Deve rejeitar documento com tamanho inválido")
        void deveRejeitarDocumentoComTamanhoInvalido() {
            RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                    () -> DocumentoValidator.validarDocumento("12345")); // 5 dígitos
            assertTrue(ex.getMessage().contains("5 dígitos"));
        }
    }

    @Nested
    @DisplayName("Formatação de documentos")
    class FormatacaoDocumentos {

        @Test
        @DisplayName("Deve formatar CPF corretamente")
        void deveFormatarCPF() {
            assertEquals("529.982.247-25", DocumentoValidator.formatarCPF("52998224725"));
        }

        @Test
        @DisplayName("Deve formatar CNPJ corretamente")
        void deveFormatarCNPJ() {
            assertEquals("11.222.333/0001-81", DocumentoValidator.formatarCNPJ("11222333000181"));
        }

        @Test
        @DisplayName("Deve identificar tipo de documento")
        void deveIdentificarTipoDocumento() {
            assertEquals("CPF", DocumentoValidator.identificarTipoDocumento("52998224725"));
            assertEquals("CNPJ", DocumentoValidator.identificarTipoDocumento("11222333000181"));
            assertEquals("INVALIDO", DocumentoValidator.identificarTipoDocumento("12345"));
            assertEquals("INVALIDO", DocumentoValidator.identificarTipoDocumento(null));
        }
    }
}
