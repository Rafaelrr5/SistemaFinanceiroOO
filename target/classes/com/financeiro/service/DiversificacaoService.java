package com.financeiro.service;

import com.financeiro.model.Carteira;
import com.financeiro.model.Ativo;
import com.financeiro.model.Acao;
import com.financeiro.model.FII;
import com.financeiro.model.Tesouro;
import com.financeiro.model.Stock;
import com.financeiro.model.Criptoativo;
import java.util.Map;

public class DiversificacaoService {

    public Map<String, Double> calcularDistribuicaoPorClasse(Carteira carteira) {
        Map<String, Double> distribuicao = new java.util.HashMap<>();
        double valorTotal = calcularValorTotalCarteira(carteira);
        
        if (valorTotal == 0) {
            distribuicao.put("Ações", 0.0);
            distribuicao.put("FIIs", 0.0);
            distribuicao.put("Tesouro", 0.0);
            distribuicao.put("Stocks", 0.0);
            distribuicao.put("Criptoativos", 0.0);
            return distribuicao;
        }
        
        double valorAcoes = 0, valorFIIs = 0, valorTesouro = 0, valorStocks = 0, valorCripto = 0;
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            double valor = quantidade * ativo.getPreco();
            
            if (ativo instanceof Acao) {
                valorAcoes += valor;
            } else if (ativo instanceof FII) {
                valorFIIs += valor;
            } else if (ativo instanceof Tesouro) {
                valorTesouro += valor;
            } else if (ativo instanceof Stock) {
                valorStocks += valor;
            } else if (ativo instanceof Criptoativo) {
                valorCripto += valor;
            }
        }
        
        distribuicao.put("Ações", (valorAcoes / valorTotal) * 100);
        distribuicao.put("FIIs", (valorFIIs / valorTotal) * 100);
        distribuicao.put("Tesouro", (valorTesouro / valorTotal) * 100);
        distribuicao.put("Stocks", (valorStocks / valorTotal) * 100);
        distribuicao.put("Criptoativos", (valorCripto / valorTotal) * 100);
        
        return distribuicao;
    }

    public Map<String, Double> calcularDistribuicaoRendaFixaVariavel(Carteira carteira) {
        Map<String, Double> distribuicao = new java.util.HashMap<>();
        double valorTotal = calcularValorTotalCarteira(carteira);
        
        if (valorTotal == 0) {
            distribuicao.put("Renda Fixa", 0.0);
            distribuicao.put("Renda Variável", 0.0);
            return distribuicao;
        }
        
        double valorRendaFixa = 0, valorRendaVariavel = 0;
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            double valor = quantidade * ativo.getPreco();
            
            if (ativo instanceof Tesouro) {
                valorRendaFixa += valor;
            } else {
                valorRendaVariavel += valor;
            }
        }
        
        distribuicao.put("Renda Fixa", (valorRendaFixa / valorTotal) * 100);
        distribuicao.put("Renda Variável", (valorRendaVariavel / valorTotal) * 100);
        
        return distribuicao;
    }

    public Map<String, Double> calcularDistribuicaoNacionalInternacional(Carteira carteira) {
        Map<String, Double> distribuicao = new java.util.HashMap<>();
        double valorTotal = calcularValorTotalCarteira(carteira);
        
        if (valorTotal == 0) {
            distribuicao.put("Nacional", 0.0);
            distribuicao.put("Internacional", 0.0);
            return distribuicao;
        }
        
        double valorNacional = 0, valorInternacional = 0;
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            double valor = quantidade * ativo.getPreco();
            
            if (ativo instanceof Stock || ativo instanceof Criptoativo) {
                valorInternacional += valor;
            } else {
                valorNacional += valor;
            }
        }
        
        distribuicao.put("Nacional", (valorNacional / valorTotal) * 100);
        distribuicao.put("Internacional", (valorInternacional / valorTotal) * 100);
        
        return distribuicao;
    }

    public double calcularIndiceConcentracao(Carteira carteira) {
        double valorTotal = calcularValorTotalCarteira(carteira);
        if (valorTotal == 0) return 0;
        
        double somaQuadrados = 0;
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            double valor = quantidade * ativo.getPreco();
            double participacao = valor / valorTotal;
            somaQuadrados += Math.pow(participacao, 2);
        }
        
        return somaQuadrados;
    }

    public int calcularQuantidadeAtivosDiferentes(Carteira carteira) {
        return carteira.getAtivos().size();
    }

    public String avaliarDiversificacao(Carteira carteira) {
        double indice = calcularIndiceConcentracao(carteira);
        int qtdAtivos = calcularQuantidadeAtivosDiferentes(carteira);
        
        if (qtdAtivos <= 1) return "Pouco diversificada (concentrada)";
        if (indice > 0.25) return "Pouco diversificada";
        if (indice > 0.15) return "Moderadamente diversificada";
        if (indice > 0.10) return "Bem diversificada";
        return "Muito bem diversificada";
    }

    private double calcularValorTotalCarteira(Carteira carteira) {
        double total = 0;
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            total += entry.getValue() * entry.getKey().getPreco();
        }
        return total;
    }
}