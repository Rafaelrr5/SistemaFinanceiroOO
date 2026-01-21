package com.financeiro.service;

import com.financeiro.model.Carteira;
import com.financeiro.model.Ativo;
import com.financeiro.model.Transacao;
import com.financeiro.repository.TransacaoRepository;
import com.financeiro.repository.CotacaoRepository;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class AnaliseService {
    private TransacaoRepository transacaoRepository;
    private CotacaoRepository cotacaoRepository;

    public AnaliseService(TransacaoRepository transacaoRepository, CotacaoRepository cotacaoRepository) {
        this.transacaoRepository = transacaoRepository;
        this.cotacaoRepository = cotacaoRepository;
    }

    public double calcularRentabilidadeTotal(Carteira carteira) {
        double investido = 0;
        double atual = 0;
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            
            investido += quantidade * buscarPrecoMedioCompra(ativo);
            atual += quantidade * ativo.getPreco();
        }
        
        if (investido == 0) return 0;
        return ((atual - investido) / investido) * 100;
    }

    private double buscarPrecoMedioCompra(Ativo ativo) {
        List<Transacao> transacoes = transacaoRepository.listarTodos();
        double totalGasto = 0;
        int totalQuantidade = 0;
        
        for (Transacao t : transacoes) {
            if (t.getAtivo().equals(ativo)) {
                totalGasto += t.getValorTotal();
                totalQuantidade += t.getQuantidade();
            }
        }
        
        return totalQuantidade > 0 ? totalGasto / totalQuantidade : ativo.getPreco();
    }

    public Map<String, Double> calcularDesempenhoPorAtivo(Carteira carteira) {
        Map<String, Double> desempenho = new java.util.HashMap<>();
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            
            double precoMedio = buscarPrecoMedioCompra(ativo);
            double valorAtual = quantidade * ativo.getPreco();
            double valorInvestido = quantidade * precoMedio;
            
            double rentabilidade = valorInvestido > 0 ? 
                ((valorAtual - valorInvestido) / valorInvestido) * 100 : 0;
            
            desempenho.put(ativo.getTicker(), rentabilidade);
        }
        
        return desempenho;
    }

    public double calcularRentabilidadePeriodo(Carteira carteira, LocalDate inicio, LocalDate fim) {
        double valorInicial = simularValorCarteiraNaData(carteira, inicio);
        double valorFinal = simularValorCarteiraNaData(carteira, fim);
        
        if (valorInicial == 0) return 0;
        return ((valorFinal - valorInicial) / valorInicial) * 100;
    }

    private double simularValorCarteiraNaData(Carteira carteira, LocalDate data) {
        double valor = 0;
        
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            
            double precoHistorico = buscarPrecoHistorico(ativo.getTicker(), data);
            valor += quantidade * precoHistorico;
        }
        
        return valor;
    }

    private double buscarPrecoHistorico(String ticker, LocalDate data) {
        List<com.financeiro.model.Cotacao> cotacoes = cotacaoRepository.buscarHistorico(ticker);
        for (com.financeiro.model.Cotacao c : cotacoes) {
            if (c.getData().equals(data) || c.getData().isBefore(data)) {
                return c.getPreco();
            }
        }
        return 0;
    }

    public double calcularRentabilidadeAnualizada(Carteira carteira) {
        if (carteira.getAtivos().isEmpty()) return 0;
        
        LocalDate dataMaisAntiga = buscarDataTransacaoMaisAntiga();
        long dias = ChronoUnit.DAYS.between(dataMaisAntiga, LocalDate.now());
        double anos = dias / 365.0;
        
        double rentabilidadeTotal = calcularRentabilidadeTotal(carteira);
        
        if (anos <= 0) return rentabilidadeTotal;
        return (Math.pow(1 + rentabilidadeTotal/100, 1/anos) - 1) * 100;
    }

    private LocalDate buscarDataTransacaoMaisAntiga() {
        List<Transacao> transacoes = transacaoRepository.listarTodos();
        if (transacoes.isEmpty()) return LocalDate.now();
        
        return transacoes.stream()
                .map(t -> t.getData().toLocalDate())
                .min(LocalDate::compareTo)
                .orElse(LocalDate.now());
    }
}