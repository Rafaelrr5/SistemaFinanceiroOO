package com.financeiro.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para Carteira e ItemCarteira.
 */
@DisplayName("Carteira")
class CarteiraTest {

    private Carteira carteira;
    private Acao petr3;
    private Acao itub4;
    private Stock aapl;

    @BeforeEach
    void setUp() {
        carteira = new Carteira("Carteira de Teste");
        petr3 = new Acao("PETR3", "Petrobras", 35.50, false);
        itub4 = new Acao("ITUB4", "Itaú", 28.90, false);
        aapl = new Stock("AAPL", "Apple", 180.00, "NASDAQ", "Tech", 5.00); // Fator 5.0 para facilitar cálculos
    }

    @Nested
    @DisplayName("Adicionar Ativos")
    class AdicionarAtivos {

        @Test
        @DisplayName("Deve adicionar novo ativo à carteira")
        void deveAdicionarNovoAtivo() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            
            assertEquals(1, carteira.getAtivos().size());
            assertTrue(carteira.getAtivos().containsKey(petr3));
            
            ItemCarteira item = carteira.getAtivos().get(petr3);
            assertEquals(100, item.getQuantidade());
            assertEquals(35.00, item.getPrecoMedioCompra());
        }

        @Test
        @DisplayName("Deve calcular preço médio ao adicionar mais do mesmo ativo")
        void deveCalcularPrecoMedio() {
            // Primeira compra: 100 ações a R$ 30,00
            carteira.adicionarAtivo(petr3, 100, 30.00);
            
            // Segunda compra: 100 ações a R$ 40,00
            carteira.adicionarAtivo(petr3, 100, 40.00);
            
            ItemCarteira item = carteira.getAtivos().get(petr3);
            
            // Total: 200 ações
            assertEquals(200, item.getQuantidade());
            
            // Preço médio: (100*30 + 100*40) / 200 = 7000 / 200 = 35
            assertEquals(35.00, item.getPrecoMedioCompra(), 0.01);
        }

        @Test
        @DisplayName("Deve adicionar múltiplos ativos diferentes")
        void deveAdicionarMultiplosAtivos() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            carteira.adicionarAtivo(itub4, 50, 28.00);
            carteira.adicionarAtivo(aapl, 10, 180.00);
            
            assertEquals(3, carteira.getAtivos().size());
        }
    }

    @Nested
    @DisplayName("Remover Ativos")
    class RemoverAtivos {

        @Test
        @DisplayName("Deve remover quantidade parcial do ativo")
        void deveRemoverQuantidadeParcial() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            carteira.removerAtivo(petr3, 30);
            
            ItemCarteira item = carteira.getAtivos().get(petr3);
            assertEquals(70, item.getQuantidade());
        }

        @Test
        @DisplayName("Deve remover ativo completamente quando quantidade >= total")
        void deveRemoverAtivoCompletamente() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            carteira.removerAtivo(petr3, 100);
            
            assertFalse(carteira.getAtivos().containsKey(petr3));
        }

        @Test
        @DisplayName("Deve remover ativo quando quantidade vendida > disponível")
        void deveRemoverAtivoQuandoQuantidadeMaior() {
            carteira.adicionarAtivo(petr3, 100, 35.00);
            carteira.removerAtivo(petr3, 150); // Tenta vender mais do que tem
            
            // Deve remover o ativo completamente
            assertFalse(carteira.getAtivos().containsKey(petr3));
        }
    }

    @Nested
    @DisplayName("Cálculo de Valores")
    class CalculoValores {

        @Test
        @DisplayName("Deve calcular valor total gasto corretamente")
        void deveCalcularValorTotalGasto() {
            carteira.adicionarAtivo(petr3, 100, 35.00); // 3500
            
            ItemCarteira item = carteira.getAtivos().get(petr3);
            assertEquals(3500.00, item.getValorTotalGasto(), 0.01);
        }

        @Test
        @DisplayName("Deve calcular valor atual corretamente para ativo nacional")
        void deveCalcularValorAtualNacional() {
            carteira.adicionarAtivo(petr3, 100, 30.00); // Comprou a 30
            // Preço atual do ativo é 35.50
            
            ItemCarteira item = carteira.getAtivos().get(petr3);
            // Valor atual: 100 * 35.50 * 1.0 (fator conversão) = 3550
            assertEquals(3550.00, item.getValorTotalAtual(), 0.01);
        }

        @Test
        @DisplayName("Deve calcular valor atual corretamente para ativo internacional")
        void deveCalcularValorAtualInternacional() {
            carteira.adicionarAtivo(aapl, 10, 180.00);
            
            ItemCarteira item = carteira.getAtivos().get(aapl);
            // Valor atual: 10 * 180 * 5.0 (fator conversão) = 9000
            assertEquals(9000.00, item.getValorTotalAtual(), 0.01);
        }

        @Test
        @DisplayName("Deve calcular valor total da carteira")
        void deveCalcularValorTotalCarteira() {
            carteira.adicionarAtivo(petr3, 100, 35.00); // Valor atual: 100 * 35.50 = 3550
            carteira.adicionarAtivo(itub4, 50, 28.00);  // Valor atual: 50 * 28.90 = 1445
            
            double valorEsperado = (100 * 35.50) + (50 * 28.90); // 3550 + 1445 = 4995
            assertEquals(valorEsperado, carteira.getValorTotalCarteira(), 0.01);
        }
    }
}
