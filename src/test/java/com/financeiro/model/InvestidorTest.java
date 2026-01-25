package com.financeiro.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para classes de Investidor.
 */
@DisplayName("Investidores")
class InvestidorTest {

    @Nested
    @DisplayName("Pessoa Física")
    class PessoaFisicaTest {

        private PessoaFisica conservador;
        private PessoaFisica moderado;
        private PessoaFisica arrojado;
        private PessoaFisica qualificado;

        @BeforeEach
        void setUp() {
            conservador = new PessoaFisica("João", "12345678901", "11999999999", 
                "joao@email.com", LocalDate.of(1990, 1, 1), "Rua A, 123", 50000.00, PerfilInvestidor.CONSERVADOR);
            
            moderado = new PessoaFisica("Maria", "98765432100", "11988888888", 
                "maria@email.com", LocalDate.of(1985, 5, 15), "Rua B, 456", 150000.00, PerfilInvestidor.MODERADO);
            
            arrojado = new PessoaFisica("Pedro", "11122233344", "11977777777", 
                "pedro@email.com", LocalDate.of(1980, 10, 20), "Rua C, 789", 500000.00, PerfilInvestidor.ARROJADO);
            
            qualificado = new PessoaFisica("Carlos", "55566677788", "11966666666", 
                "carlos@email.com", LocalDate.of(1975, 3, 10), "Av D, 1000", 1500000.00, PerfilInvestidor.ARROJADO);
        }

        @Test
        @DisplayName("Investidor com patrimônio >= 1M deve ser qualificado")
        void investidorDeveSerQualificado() {
            assertTrue(qualificado.isQualificado());
            assertFalse(conservador.isQualificado());
            assertFalse(moderado.isQualificado());
            assertFalse(arrojado.isQualificado());
        }

        @Test
        @DisplayName("Conservador NÃO pode operar Criptoativos")
        void conservadorNaoPodeOperarCripto() {
            Criptoativo btc = new Criptoativo("BTC", "Bitcoin", 50000.00, "PoW", 21000000, 5.39);
            assertFalse(conservador.podeMovimentar(btc));
        }

        @Test
        @DisplayName("Moderado NÃO pode operar Criptoativos")
        void moderadoNaoPodeOperarCripto() {
            Criptoativo btc = new Criptoativo("BTC", "Bitcoin", 50000.00, "PoW", 21000000, 5.39);
            assertFalse(moderado.podeMovimentar(btc));
        }

        @Test
        @DisplayName("Arrojado PODE operar Criptoativos")
        void arrojadoPodeOperarCripto() {
            Criptoativo btc = new Criptoativo("BTC", "Bitcoin", 50000.00, "PoW", 21000000, 5.39);
            assertTrue(arrojado.podeMovimentar(btc));
        }

        @Test
        @DisplayName("Conservador NÃO pode operar Stocks")
        void conservadorNaoPodeOperarStocks() {
            Stock aapl = new Stock("AAPL", "Apple", 180.00, "NASDAQ", "Tech", 5.39);
            assertFalse(conservador.podeMovimentar(aapl));
        }

        @Test
        @DisplayName("Moderado PODE operar Stocks")
        void moderadoPodeOperarStocks() {
            Stock aapl = new Stock("AAPL", "Apple", 180.00, "NASDAQ", "Tech", 5.39);
            assertTrue(moderado.podeMovimentar(aapl));
        }

        @Test
        @DisplayName("Arrojado PODE operar Stocks")
        void arrojadoPodeOperarStocks() {
            Stock aapl = new Stock("AAPL", "Apple", 180.00, "NASDAQ", "Tech", 5.39);
            assertTrue(arrojado.podeMovimentar(aapl));
        }

        @Test
        @DisplayName("Todos podem operar Ações")
        void todosPodeOperarAcoes() {
            Acao petr3 = new Acao("PETR3", "Petrobras", 35.50, false);
            assertTrue(conservador.podeMovimentar(petr3));
            assertTrue(moderado.podeMovimentar(petr3));
            assertTrue(arrojado.podeMovimentar(petr3));
        }

        @Test
        @DisplayName("Não qualificado NÃO pode operar ativo qualificado")
        void naoQualificadoNaoPodeOperarAtivoQualificado() {
            Acao ativoQualificado = new Acao("XPTO3", "Ativo Restrito", 100.00, true);
            assertFalse(conservador.podeMovimentar(ativoQualificado));
            assertFalse(moderado.podeMovimentar(ativoQualificado));
            assertFalse(arrojado.podeMovimentar(ativoQualificado));
        }

        @Test
        @DisplayName("Qualificado PODE operar ativo qualificado")
        void qualificadoPodeOperarAtivoQualificado() {
            Acao ativoQualificado = new Acao("XPTO3", "Ativo Restrito", 100.00, true);
            assertTrue(qualificado.podeMovimentar(ativoQualificado));
        }
    }

    @Nested
    @DisplayName("Institucional")
    class InstitucionalTest {

        private Institucional institucional;

        @BeforeEach
        void setUp() {
            institucional = new Institucional("Fundo XYZ", "12345678000199", "11955555555",
                "contato@xyz.com", LocalDate.of(2010, 1, 1), "Av Paulista, 1000", 10000000.00, "XYZ Gestora LTDA");
        }

        @Test
        @DisplayName("Institucional pode operar qualquer tipo de ativo")
        void institucionalPodeOperarQualquerAtivo() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            FII fii = new FII("HGLG11", "CSHG Logística", 160.00, "Logística", 1.20, 0.75);
            Stock stock = new Stock("AAPL", "Apple", 180.00, "NASDAQ", "Tech", 5.39);
            Criptoativo cripto = new Criptoativo("BTC", "Bitcoin", 50000.00, "PoW", 21000000, 5.39);
            Acao ativoQualificado = new Acao("XPTO3", "Ativo Restrito", 100.00, true);
            
            assertTrue(institucional.podeMovimentar(acao));
            assertTrue(institucional.podeMovimentar(fii));
            assertTrue(institucional.podeMovimentar(stock));
            assertTrue(institucional.podeMovimentar(cripto));
            assertTrue(institucional.podeMovimentar(ativoQualificado));
        }

        @Test
        @DisplayName("Deve retornar razão social")
        void deveRetornarRazaoSocial() {
            assertEquals("XYZ Gestora LTDA", institucional.getRazaoSocial());
        }
    }
}
