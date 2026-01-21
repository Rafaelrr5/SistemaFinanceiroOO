package com.financeiro.service;

import com.financeiro.model.Carteira;
import java.util.List;
import java.util.Map;

public class ComparativoService {

    public Map<String, Double> compararCarteiras(List<Carteira> carteiras) {
        Map<String, Double> comparativo = new java.util.HashMap<>();
        
        for (Carteira carteira : carteiras) {
            double valorTotal = calcularValorTotal(carteira);
            comparativo.put(carteira.getNome(), valorTotal);
        }
        
        return comparativo;
    }

    public Map<String, Double> compararRentabilidade(List<Carteira> carteiras, AnaliseService analiseService) {
        Map<String, Double> comparativo = new java.util.HashMap<>();
        
        for (Carteira carteira : carteiras) {
            double rentabilidade = analiseService.calcularRentabilidadeTotal(carteira);
            comparativo.put(carteira.getNome(), rentabilidade);
        }
        
        return comparativo;
    }

    public Map<String, String> compararDiversificacao(List<Carteira> carteiras, DiversificacaoService diversificacaoService) {
        Map<String, String> comparativo = new java.util.HashMap<>();
        
        for (Carteira carteira : carteiras) {
            String avaliacao = diversificacaoService.avaliarDiversificacao(carteira);
            comparativo.put(carteira.getNome(), avaliacao);
        }
        
        return comparativo;
    }

    public Carteira identificarMelhorCarteira(List<Carteira> carteiras, AnaliseService analiseService) {
        if (carteiras.isEmpty()) return null;
        
        Carteira melhor = carteiras.get(0);
        double melhorRentabilidade = analiseService.calcularRentabilidadeTotal(melhor);
        
        for (int i = 1; i < carteiras.size(); i++) {
            Carteira atual = carteiras.get(i);
            double rentabilidadeAtual = analiseService.calcularRentabilidadeTotal(atual);
            
            if (rentabilidadeAtual > melhorRentabilidade) {
                melhor = atual;
                melhorRentabilidade = rentabilidadeAtual;
            }
        }
        
        return melhor;
    }

    public Map<String, Map<String, Double>> compararDistribuicoes(List<Carteira> carteiras, DiversificacaoService diversificacaoService) {
        Map<String, Map<String, Double>> comparativo = new java.util.HashMap<>();
        
        for (Carteira carteira : carteiras) {
            Map<String, Double> distribuicao = diversificacaoService.calcularDistribuicaoPorClasse(carteira);
            comparativo.put(carteira.getNome(), distribuicao);
        }
        
        return comparativo;
    }

    public double calcularBenchmark(List<Carteira> carteiras) {
        if (carteiras.isEmpty()) return 0;
        
        double somaRentabilidades = 0;
        int count = 0;
        
        for (Carteira carteira : carteiras) {
            if (!carteira.getAtivos().isEmpty()) {
                somaRentabilidades += calcularValorTotal(carteira);
                count++;
            }
        }
        
        return count > 0 ? somaRentabilidades / count : 0;
    }

    private double calcularValorTotal(Carteira carteira) {
        double total = 0;
        for (Map.Entry<com.financeiro.model.Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            total += entry.getValue() * entry.getKey().getPreco();
        }
        return total;
    }
}
