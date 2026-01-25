package com.financeiro.repository;

import com.financeiro.model.Cotacao;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CotacaoRepository {
    private Map<String, List<Cotacao>> historico = new HashMap<>();

    public CotacaoRepository() {
        inicializarDados();
    }

    private void inicializarDados() {
        adicionarCotacao("PETR4", LocalDate.now().minusDays(10), 28.50, 1.5);
        adicionarCotacao("PETR4", LocalDate.now().minusDays(5), 29.80, 4.5);
        adicionarCotacao("PETR4", LocalDate.now(), 29.83, 0.1);

        adicionarCotacao("ITUB4", LocalDate.now().minusDays(10), 38.00, 2.0);
        adicionarCotacao("ITUB4", LocalDate.now().minusDays(5), 39.10, 2.9);
        adicionarCotacao("ITUB4", LocalDate.now(), 39.32, 0.6);

        adicionarCotacao("BTC", LocalDate.now().minusDays(10), 42000, 5.2);
        adicionarCotacao("BTC", LocalDate.now().minusDays(5), 44000, 4.8);
        adicionarCotacao("BTC", LocalDate.now(), 45000, 2.3);
    }

    private void adicionarCotacao(String ticker, LocalDate data, double preco, double variacao) {
        historico.computeIfAbsent(ticker, k -> new ArrayList<>())
                .add(new Cotacao(data, preco, variacao));
    }

    public List<Cotacao> buscarHistorico(String ticker) {
        return new ArrayList<>(historico.getOrDefault(ticker, new ArrayList<>()));
    }

    public Cotacao buscarCotacaoAtual(String ticker) {
        List<Cotacao> cotacoes = historico.get(ticker);
        if (cotacoes != null && !cotacoes.isEmpty()) {
            return cotacoes.get(cotacoes.size() - 1);
        }
        return null;
    }

    public void adicionarCotacao(String ticker, Cotacao cotacao) {
        historico.computeIfAbsent(ticker, k -> new ArrayList<>()).add(cotacao);
    }

    public Map<String, List<Cotacao>> getTodoHistorico() {
        return new HashMap<>(historico);
    }
}
