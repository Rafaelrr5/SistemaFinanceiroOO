package com.financeiro.service;

import com.financeiro.exception.RegraNegocioException;
import com.financeiro.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para ValidacaoService.
 */
@DisplayName("ValidacaoService")
class ValidacaoServiceTest {

    private ValidacaoService validacaoService;
    private Carteira carteira;
    private Acao petr3;
    private Stock aapl;
    private Criptoativo btc;

    @BeforeEach
    void setUp() {
        validacaoService = new ValidacaoService();
        carteira = new Carteira("Carteira Teste");
        petr3 = new Acao("PETR3", "Petrobras", 35.50, false);
        aapl = new Stock("AAPL", "Apple", 180.00, "NASDAQ", "Tech", 5.39);
        btc = new Criptoativo("BTC", "Bitcoin", 50000.00, "PoW", 21000000, 5.39);
    }

    @Nested
    @DisplayName("Validação de Quantidade")
    class ValidacaoQuantidade {

        @Test
        @DisplayName("Deve aceitar quantidade positiva")
        void deveAceitarQuantidadePositiva() {
            assertDoesNotThrow(() -> validacaoService.validarQuantidadePositiva(10));
            assertDoesNotThrow(() -> validacaoService.validarQuantidadePositiva(0.5));
        }

        @Test
        @DisplayName("Deve rejeitar quantidade zero ou negativa")
        void deveRejeitarQuantidadeZeroOuNegativa() {
            assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarQuantidadePositiva(0));
            assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarQuantidadePositiva(-5));
        }
    }

    @Nested
    @DisplayName("Validação de Preço")
    class ValidacaoPreco {

        @Test
        @DisplayName("Deve aceitar preço positivo")
        void deveAceitarPrecoPositivo() {
            assertDoesNotThrow(() -> validacaoService.validarPrecoPositivo(100.50));
        }

        @Test
        @DisplayName("Deve rejeitar preço zero ou negativo")
        void deveRejeitarPrecoZeroOuNegativo() {
            assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarPrecoPositivo(0));
            assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarPrecoPositivo(-10));
        }
    }

    @Nested
    @DisplayName("Validação de Quantidade de Ativo na Carteira")
    class ValidacaoQuantidadeAtivo {

        @Test
        @DisplayName("Deve aceitar venda quando há quantidade suficiente")
        void deveAceitarVendaComQuantidadeSuficiente() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            assertDoesNotThrow(() -> validacaoService.validarQuantidadeAtivo(carteira, petr3, 50));
        }

        @Test
        @DisplayName("Deve rejeitar venda quando não há quantidade suficiente")
        void deveRejeitarVendaSemQuantidadeSuficiente() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            RegraNegocioException ex = assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarQuantidadeAtivo(carteira, petr3, 150));
            assertTrue(ex.getMessage().contains("insuficiente"));
        }

        @Test
        @DisplayName("Deve rejeitar venda de ativo não existente na carteira")
        void deveRejeitarVendaAtivoInexistente() {
            assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarQuantidadeAtivo(carteira, petr3, 10));
        }
    }

    @Nested
    @DisplayName("Validação de Permissão de Investimento")
    class ValidacaoPermissao {

        @Test
        @DisplayName("Conservador não pode operar Criptoativos")
        void conservadorNaoPodeOperarCripto() {
            PessoaFisica conservador = criarPessoaFisica(PerfilInvestidor.CONSERVADOR, 50000);
            
            RegraNegocioException ex = assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarPermissaoInvestimento(conservador, btc));
            assertTrue(ex.getMessage().contains("ARROJADO"));
        }

        @Test
        @DisplayName("Conservador não pode operar Stocks")
        void conservadorNaoPodeOperarStocks() {
            PessoaFisica conservador = criarPessoaFisica(PerfilInvestidor.CONSERVADOR, 50000);
            
            RegraNegocioException ex = assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarPermissaoInvestimento(conservador, aapl));
            assertTrue(ex.getMessage().contains("CONSERVADOR"));
        }

        @Test
        @DisplayName("Moderado pode operar Stocks")
        void moderadoPodeOperarStocks() {
            PessoaFisica moderado = criarPessoaFisica(PerfilInvestidor.MODERADO, 150000);
            assertDoesNotThrow(() -> validacaoService.validarPermissaoInvestimento(moderado, aapl));
        }

        @Test
        @DisplayName("Arrojado pode operar Criptoativos")
        void arrojadoPodeOperarCripto() {
            PessoaFisica arrojado = criarPessoaFisica(PerfilInvestidor.ARROJADO, 500000);
            assertDoesNotThrow(() -> validacaoService.validarPermissaoInvestimento(arrojado, btc));
        }

        @Test
        @DisplayName("Não qualificado não pode operar ativo qualificado")
        void naoQualificadoNaoPodeOperarAtivoQualificado() {
            PessoaFisica naoQualificado = criarPessoaFisica(PerfilInvestidor.ARROJADO, 500000);
            Acao ativoQualificado = new Acao("XPTO3", "Restrito", 100.00, true);
            
            RegraNegocioException ex = assertThrows(RegraNegocioException.class, 
                () -> validacaoService.validarPermissaoInvestimento(naoQualificado, ativoQualificado));
            assertTrue(ex.getMessage().contains("qualificados"));
        }

        @Test
        @DisplayName("Qualificado pode operar ativo qualificado")
        void qualificadoPodeOperarAtivoQualificado() {
            PessoaFisica qualificado = criarPessoaFisica(PerfilInvestidor.ARROJADO, 1500000);
            Acao ativoQualificado = new Acao("XPTO3", "Restrito", 100.00, true);
            
            assertDoesNotThrow(() -> validacaoService.validarPermissaoInvestimento(qualificado, ativoQualificado));
        }

        @Test
        @DisplayName("Institucional pode operar qualquer ativo")
        void institucionalPodeOperarQualquerAtivo() {
            Institucional institucional = new Institucional("Fundo", "12345678000199", 
                "11999999999", "fundo@email.com", java.time.LocalDate.now(), "Av Paulista", 10000000, "Fundo LTDA");
            
            assertDoesNotThrow(() -> validacaoService.validarPermissaoInvestimento(institucional, btc));
            assertDoesNotThrow(() -> validacaoService.validarPermissaoInvestimento(institucional, aapl));
            assertDoesNotThrow(() -> validacaoService.validarPermissaoInvestimento(institucional, petr3));
        }
    }

    private PessoaFisica criarPessoaFisica(PerfilInvestidor perfil, double patrimonio) {
        return new PessoaFisica("Teste", "12345678901", "11999999999", 
            "teste@email.com", java.time.LocalDate.of(1990, 1, 1), "Rua Teste", patrimonio, perfil);
    }
}
