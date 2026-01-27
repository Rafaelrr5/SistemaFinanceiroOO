package com.financeiro.service;

import com.financeiro.model.TipoTransacao;
import com.financeiro.model.Transacao;
import com.financeiro.model.Ativo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ImportacaoMovimentacaoService {

    private TransacaoService transacaoService;
    private com.financeiro.repository.AtivoRepository ativoRepository;

    public ImportacaoMovimentacaoService(TransacaoService transacaoService,
            com.financeiro.repository.AtivoRepository ativoRepository) {
        this.transacaoService = transacaoService;
        this.ativoRepository = ativoRepository;
    }

    public void importarLoteMovimentacoes(String caminhoArquivo, com.financeiro.model.Investidor investidor) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                if (linha.trim().isEmpty())
                    continue;

                String[] dados = linha.split(";");
                if (dados.length < 5)
                    continue;

                try {
                    String tipo = dados[0].trim();
                    String ticker = dados[1].trim();
                    double qtd = Double.parseDouble(dados[2].replace(",", "."));
                    double preco = Double.parseDouble(dados[3].replace(",", "."));
                    String inst = dados[4].trim();

                    Ativo ativo = ativoRepository.buscarPorTicker(ticker);
                    if (ativo == null) {
                        System.out.println("Ativo nao encontrado: " + ticker);
                        continue;
                    }

                    if (tipo.equalsIgnoreCase("COMPRA")) {
                        transacaoService.comprar(investidor, ativo, qtd, preco, inst);
                    } else if (tipo.equalsIgnoreCase("VENDA")) {
                        transacaoService.vender(investidor, ativo, qtd, preco, inst);
                    }
                } catch (Exception e) {
                    System.out.println("Erro linha " + linha + ": " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro arquivo: " + e.getMessage());
        }
    }
}
