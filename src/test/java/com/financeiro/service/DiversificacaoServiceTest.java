package com.financeiro.service;

import com.financeiro.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para DiversificacaoService.
 */
@DisplayName("DiversificacaoService")
class DiversificacaoServiceTest {

    private DiversificacaoService diversificacaoService;
    private Carteira carteira;

    @BeforeEach
    void setUp() {
        diversificacaoService = new DiversificacaoService();
        carteira = new Carteira("Carteira Teste");
    }

    @Nested
    @DisplayName("Distribuição Renda Fixa vs Variável")
    class DistribuicaoRendaFixaVariavel {

        @Test
        @DisplayName("Carteira vazia deve retornar 0% para ambos")
        void carteiraVaziaDeveRetornarZero() {
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(carteira);
            assertEquals(0.0, dist.get("Renda Fixa"));
            assertEquals(0.0, dist.get("Renda Variável"));
        }

        @Test
        @DisplayName("Carteira só com Tesouro deve ser 100% Renda Fixa")
        void carteiraSoTesouroDeveSer100RendaFixa() {
            Tesouro tesouro = new Tesouro("TESOURO-SELIC", "Tesouro Selic", 100.00, "Selic", "2029");
            carteira.adicionarAtivo(tesouro, 10, 100.00);
            
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(carteira);
            assertEquals(100.0, dist.get("Renda Fixa"), 0.01);
            assertEquals(0.0, dist.get("Renda Variável"), 0.01);
        }

        @Test
        @DisplayName("Carteira só com Ações deve ser 100% Renda Variável")
        void carteiraSoAcoesDeveSer100RendaVariavel() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            carteira.adicionarAtivo(acao, 100, 35.00);
            
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(carteira);
            assertEquals(0.0, dist.get("Renda Fixa"), 0.01);
            assertEquals(100.0, dist.get("Renda Variável"), 0.01);
        }

        @Test
        @DisplayName("Carteira mista deve calcular proporção correta")
        void carteiraMistaDeveCalcularProporcao() {
            // Tesouro: 1000 (10 * 100)
            Tesouro tesouro = new Tesouro("TESOURO-SELIC", "Tesouro Selic", 100.00, "Selic", "2029");
            carteira.adicionarAtivo(tesouro, 10, 100.00);
            
            // Ação: 1000 (100 * 10) - preço atual também é 10 para facilitar
            Acao acao = new Acao("TEST3", "Teste", 10.00, false);
            carteira.adicionarAtivo(acao, 100, 10.00);
            
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(carteira);
            // Total: 2000, cada um representa 50%
            assertEquals(50.0, dist.get("Renda Fixa"), 0.01);
            assertEquals(50.0, dist.get("Renda Variável"), 0.01);
        }
    }

    @Nested
    @DisplayName("Distribuição Nacional vs Internacional")
    class DistribuicaoNacionalInternacional {

        @Test
        @DisplayName("Carteira vazia deve retornar 0% para ambos")
        void carteiraVaziaDeveRetornarZero() {
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoNacionalInternacional(carteira);
            assertEquals(0.0, dist.get("Nacional"));
            assertEquals(0.0, dist.get("Internacional"));
        }

        @Test
        @DisplayName("Carteira só com ativos nacionais deve ser 100% Nacional")
        void carteiraSoNacionalDeveSer100Nacional() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            carteira.adicionarAtivo(acao, 100, 35.00);
            
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoNacionalInternacional(carteira);
            assertEquals(100.0, dist.get("Nacional"), 0.01);
            assertEquals(0.0, dist.get("Internacional"), 0.01);
        }

        @Test
        @DisplayName("Carteira só com ativos internacionais deve ser 100% Internacional")
        void carteiraSoInternacionalDeveSer100Internacional() {
            // Stock com fator 1.0 para facilitar cálculo
            Stock stock = new Stock("AAPL", "Apple", 100.00, "NASDAQ", "Tech", 1.0);
            carteira.adicionarAtivo(stock, 10, 100.00);
            
            Map<String, Double> dist = diversificacaoService.calcularDistribuicaoNacionalInternacional(carteira);
            assertEquals(0.0, dist.get("Nacional"), 0.01);
            assertEquals(100.0, dist.get("Internacional"), 0.01);
        }
    }

    @Nested
    @DisplayName("Avaliação de Diversificação")
    class AvaliacaoDiversificacao {

        @Test
        @DisplayName("Carteira com 1 ativo deve ser pouco diversificada")
        void carteiraUmAtivoDeveSerpoucoDiversificada() {
            Acao acao = new Acao("PETR3", "Petrobras", 35.50, false);
            carteira.adicionarAtivo(acao, 100, 35.00);
            
            String avaliacao = diversificacaoService.avaliarDiversificacao(carteira);
            assertTrue(avaliacao.toLowerCase().contains("pouco") || avaliacao.toLowerCase().contains("concentrada"));
        }

        @Test
        @DisplayName("Deve contar quantidade de ativos diferentes")
        void deveContarQuantidadeAtivos() {
            Acao acao1 = new Acao("PETR3", "Petrobras", 35.50, false);
            Acao acao2 = new Acao("ITUB4", "Itaú", 28.90, false);
            FII fii = new FII("HGLG11", "CSHG Logística", 160.00, "Logística", 1.20, 0.75);
            
            carteira.adicionarAtivo(acao1, 100, 35.00);
            carteira.adicionarAtivo(acao2, 50, 28.00);
            carteira.adicionarAtivo(fii, 20, 160.00);
            
            assertEquals(3, diversificacaoService.calcularQuantidadeAtivosDiferentes(carteira));
        }
    }
}
