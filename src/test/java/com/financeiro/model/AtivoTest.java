package com.financeiro.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para classes de Ativo.
 */
@DisplayName("Ativos")
class AtivoTest {

    @Nested
    @DisplayName("Ação")
    class AcaoTest {

        @Test
        @DisplayName("Deve identificar ação ordinária (termina em 3)")
        void deveIdentificarAcaoOrdinaria() {
            Acao acao = new Acao("PETR3", "Petrobras ON", 35.50, false);
            assertEquals("Ordinária", acao.getTipoAcao());
        }

        @Test
        @DisplayName("Deve identificar ação preferencial (termina em 4)")
        void deveIdentificarAcaoPreferencial4() {
            Acao acao = new Acao("ITSA4", "Itaúsa PN", 10.20, false);
            assertEquals("Preferencial", acao.getTipoAcao());
        }

        @Test
        @DisplayName("Deve identificar ação preferencial (termina em 5)")
        void deveIdentificarAcaoPreferencial5() {
            Acao acao = new Acao("ELET5", "Eletrobras PNA", 45.00, false);
            assertEquals("Preferencial", acao.getTipoAcao());
        }

        @Test
        @DisplayName("Deve identificar ação preferencial (termina em 6)")
        void deveIdentificarAcaoPreferencial6() {
            Acao acao = new Acao("ELET6", "Eletrobras PNB", 44.00, false);
            assertEquals("Preferencial", acao.getTipoAcao());
        }

        @Test
        @DisplayName("Deve identificar ação unit (termina em 11)")
        void deveIdentificarAcaoUnit() {
            Acao acao = new Acao("TAEE11", "Taesa Unit", 35.00, false);
            assertEquals("Unit", acao.getTipoAcao());
        }

        @Test
        @DisplayName("Ação deve ser nacional")
        void acaoDeveSerNacional() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            assertTrue(acao.isNacional());
            assertFalse(acao.isInternacional());
        }

        @Test
        @DisplayName("Ação deve ser renda variável")
        void acaoDeveSerRendaVariavel() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            assertTrue(acao.isRendaVariavel());
            assertFalse(acao.isRendaFixa());
        }

        @Test
        @DisplayName("Ação deve retornar preço em reais sem conversão")
        void acaoDeveRetornarPrecoEmReais() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            assertEquals(35.50, acao.getPrecoEmReais(), 0.01);
            assertEquals(35.50, acao.getValorEmReais(), 0.01);
        }
    }

    @Nested
    @DisplayName("FII")
    class FIITest {

        @Test
        @DisplayName("Deve retornar taxa de administração formatada com %")
        void deveRetornarTaxaFormatada() {
            FII fii = new FII("HGLG11", "CSHG Logística", 160.00, "Logística", 1.20, 0.75);
            assertEquals("0.75%", fii.getTaxaAdministracaoFormatada());
        }

        @Test
        @DisplayName("FII deve ser nacional")
        void fiiDeveSerNacional() {
            FII fii = new FII("HGLG11", "CSHG Logística", 160.00, "Logística", 1.20, 0.75);
            assertTrue(fii.isNacional());
        }

        @Test
        @DisplayName("FII deve ser renda variável")
        void fiiDeveSerRendaVariavel() {
            FII fii = new FII("HGLG11", "CSHG Logística", 160.00, "Logística", 1.20, 0.75);
            assertTrue(fii.isRendaVariavel());
        }
    }

    @Nested
    @DisplayName("Tesouro")
    class TesouroTest {

        @Test
        @DisplayName("Tesouro deve ser renda fixa")
        void tesouroDeveSerRendaFixa() {
            Tesouro tesouro = new Tesouro("TESOURO-SELIC-2029", "Tesouro Selic 2029", 12500.00, "Selic", "2029-03-01");
            assertTrue(tesouro.isRendaFixa());
            assertFalse(tesouro.isRendaVariavel());
        }

        @Test
        @DisplayName("Tesouro deve ser nacional")
        void tesouroDeveSerNacional() {
            Tesouro tesouro = new Tesouro("TESOURO-SELIC-2029", "Tesouro Selic 2029", 12500.00, "Selic", "2029-03-01");
            assertTrue(tesouro.isNacional());
        }
    }

    @Nested
    @DisplayName("Stock")
    class StockTest {

        @Test
        @DisplayName("Stock deve ser internacional")
        void stockDeveSerInternacional() {
            Stock stock = new Stock("AAPL", "Apple Inc", 180.00, "NASDAQ", "Tecnologia", 5.39);
            assertTrue(stock.isInternacional());
            assertFalse(stock.isNacional());
        }

        @Test
        @DisplayName("Stock deve ser renda variável")
        void stockDeveSerRendaVariavel() {
            Stock stock = new Stock("AAPL", "Apple Inc", 180.00, "NASDAQ", "Tecnologia", 5.39);
            assertTrue(stock.isRendaVariavel());
        }

        @Test
        @DisplayName("Deve converter preço para reais")
        void deveConverterPrecoParaReais() {
            Stock stock = new Stock("AAPL", "Apple Inc", 100.00, "NASDAQ", "Tecnologia", 5.00);
            assertEquals(500.00, stock.converterParaReais(), 0.01);
        }
    }

    @Nested
    @DisplayName("Criptoativo")
    class CriptoativoTest {

        @Test
        @DisplayName("Criptoativo deve ser internacional")
        void criptoativoDeveSerInternacional() {
            Criptoativo cripto = new Criptoativo("BTC", "Bitcoin", 50000.00, "Proof of Work", 21000000, 5.39);
            assertTrue(cripto.isInternacional());
            assertFalse(cripto.isNacional());
        }

        @Test
        @DisplayName("Criptoativo deve ser renda variável")
        void criptoativoDeveSerRendaVariavel() {
            Criptoativo cripto = new Criptoativo("BTC", "Bitcoin", 50000.00, "Proof of Work", 21000000, 5.39);
            assertTrue(cripto.isRendaVariavel());
        }

        @Test
        @DisplayName("Deve converter preço para reais")
        void deveConverterPrecoParaReais() {
            Criptoativo cripto = new Criptoativo("BTC", "Bitcoin", 1000.00, "Proof of Work", 21000000, 5.00);
            assertEquals(5000.00, cripto.converterParaReais(), 0.01);
        }
    }
}
