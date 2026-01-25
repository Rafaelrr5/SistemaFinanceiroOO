package com.financeiro.service;

import com.financeiro.model.*;
import com.financeiro.repository.AtivoRepository;
import com.financeiro.repository.InvestidorRepository;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class ArquivoService {

    private AtivoRepository ativoRepository;
    private InvestidorRepository investidorRepository;

    public ArquivoService(AtivoRepository ativoRepository, InvestidorRepository investidorRepository) {
        this.ativoRepository = ativoRepository;
        this.investidorRepository = investidorRepository;
    }

    public void importarAtivos(String caminhoArquivo, String tipoAtivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) { primeira = false; continue; } // Ignora header
                if (linha.trim().isEmpty()) continue;

                Ativo ativo = null;
                try {
                    switch (tipoAtivo.toUpperCase()) {
                        case "ACAO": ativo = parseAcao(linha); break;
                        case "FII": ativo = parseFII(linha); break;
                        case "STOCK": ativo = parseStock(linha); break;
                        case "TESOURO": ativo = parseTesouro(linha); break;
                        case "CRIPTO": ativo = parseCripto(linha); break;
                    }
                    if (ativo != null) {
                        ativoRepository.salvar(ativo);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao importar linha: " + linha + " - " + e.getMessage());
                }
            }
        }
    }

    public void importarInvestidores(String caminhoArquivo) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) { primeira = false; continue; }
                if (linha.trim().isEmpty()) continue;

                try {
                    Investidor investidor = parseInvestidor(linha);
                    if (investidor != null) {
                        investidorRepository.salvar(investidor);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao importar investidor: " + linha + " - " + e.getMessage());
                }
            }
        }
    }
    
    public void importarTransacoes(String caminhoArquivo, Investidor investidor, TransacaoService transacaoService) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) { primeira = false; continue; }
                if (linha.trim().isEmpty()) continue;

                try {
                    // Formato esperado: Ticker;Tipo(C/V);Quantidade;Preco;Instituicao
                    String[] dados = linha.split(";");
                    if (dados.length < 5) continue;

                    String ticker = dados[0].trim();
                    String tipo = dados[1].trim().toUpperCase();
                    double quantidade = Double.parseDouble(dados[2].replace(",", "."));
                    double preco = Double.parseDouble(dados[3].replace(",", "."));
                    String instituicao = dados[4].trim();

                    Ativo ativo = ativoRepository.buscarPorTicker(ticker);
                    if (ativo == null) {
                        System.out.println("Ativo não encontrado: " + ticker);
                        continue;
                    }

                    if (tipo.startsWith("C")) {
                        transacaoService.comprar(investidor, ativo, quantidade, preco, instituicao);
                    } else if (tipo.startsWith("V")) {
                        transacaoService.vender(investidor, ativo, quantidade, preco, instituicao);
                    } else {
                        System.out.println("Tipo de transação desconhecido: " + tipo);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao importar transação: " + linha + " - " + e.getMessage());
                }
            }
        }
    }

    // Método auxiliar de parsing (simplificado)
    private Ativo parseAcao(String linha) {
        String[] dados = linha.split(";");
        String ticker = dados[0].trim();
        String nome = dados[1].trim();
        double preco = Double.parseDouble(dados[2].replace(",", "."));
        boolean qualificado = "1".equals(dados[3].trim());
        return new Acao(ticker, nome, preco, qualificado);
    }

    private Ativo parseFII(String linha) {
        String[] dados = linha.split(";");
        String ticker = dados[0].trim();
        String nome = dados[1].trim();
        String setor = dados[2].trim();
        double preco = Double.parseDouble(dados[3].replace(",", "."));
        double div = Double.parseDouble(dados[4].replace(",", "."));
        double taxa = Double.parseDouble(dados[5].replace(",", "."));
        return new FII(ticker, nome, preco, setor, div, taxa);
    }
    
    private Ativo parseStock(String linha) {
        String[] dados = linha.split(";");
        // Ticker;Nome;Preco;Bolsa;Setor
        return new Stock(dados[0].trim(), dados[1].trim(), 
            Double.parseDouble(dados[2].replace(",", ".")), 
            dados[3].trim(), dados[4].trim(), 5.39); // Fixo 5.39 conforme PDF
    }
    
    private Ativo parseCripto(String linha) {
        String[] dados = linha.split(";");
        // Ticker;Nome;Preco;Algoritmo;QtdMax
        return new Criptoativo(dados[0].trim(), dados[1].trim(), 
            Double.parseDouble(dados[2].replace(",", ".")), 
            dados[3].trim(), Double.parseDouble(dados[4].replace(",", ".")), 5.39);
    }
    
    private Ativo parseTesouro(String linha) {
        String[] dados = linha.split(";");
        // Ticker;Nome;Preco;Tipo;Vencimento
        return new Tesouro(dados[0].trim(), dados[1].trim(), 
            Double.parseDouble(dados[2].replace(",", ".")), 
            dados[3].trim(), dados[4].trim());
    }

    private Investidor parseInvestidor(String linha) {
        String[] dados = linha.split(";");
        // TIPO;NOME;IDENTIFICADOR;TELEFONE;EMAIL;ENDERECO;PATRIMONIO;PERFIL_OU_RAZAO
        if (dados.length < 8) return null;

        String tipo = dados[0].trim();
        String nome = dados[1].trim();
        String id = dados[2].trim();
        String tel = dados[3].trim();
        String email = dados[4].trim();
        String endereco = dados[5].trim();
        double patrimonio = Double.parseDouble(dados[6].replace(",", "."));
        String extra = dados[7].trim(); // Perfil ou RazaoSocial

        if (tipo.equalsIgnoreCase("PF")) {
            PerfilInvestidor perfil = PerfilInvestidor.valueOf(extra.toUpperCase());
            return new PessoaFisica(nome, id, tel, email, LocalDate.now(), endereco, patrimonio, perfil);
        } else if (tipo.equalsIgnoreCase("PJ")) {
            return new Institucional(nome, id, tel, email, LocalDate.now(), endereco, patrimonio, extra);
        }
        return null;
    }
}
