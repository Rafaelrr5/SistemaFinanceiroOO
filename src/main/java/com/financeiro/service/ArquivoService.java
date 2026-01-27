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
                if (primeira) {
                    primeira = false;
                    continue;
                }
                if (linha.trim().isEmpty())
                    continue;

                Ativo ativo = null;
                try {
                    switch (tipoAtivo.toUpperCase()) {
                        case "ACAO":
                            ativo = parseAcao(linha);
                            break;
                        case "FII":
                            ativo = parseFII(linha);
                            break;
                        case "STOCK":
                            ativo = parseStock(linha);
                            break;
                        case "TESOURO":
                            ativo = parseTesouro(linha);
                            break;
                        case "CRIPTO":
                            ativo = parseCripto(linha);
                            break;
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
                if (primeira) {
                    primeira = false;
                    continue;
                }
                if (linha.trim().isEmpty())
                    continue;

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

    public void importarTransacoes(String caminhoArquivo, Investidor investidor, TransacaoService transacaoService)
            throws IOException {
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

                try {
                    String[] dados = linha.split(";");
                    if (dados.length < 5)
                        continue;

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

    /**
     * Método auxiliar para converter string numérica de forma robusta.
     * Trata casos como: "-", valores vazios, separadores de milhar (1.234,56).
     */
    private double parseNumber(String valor) {
        if (valor == null || valor.trim().isEmpty() || valor.trim().equals("-")) {
            return 0.0;
        }
        String limpo = valor.trim();
        if (limpo.contains(",")) {
            limpo = limpo.replace(".", "").replace(",", ".");
        }
        return Double.parseDouble(limpo);
    }

    /**
     * Obtém valor do array de forma segura, retornando string vazia se índice
     * inválido.
     */
    private String getField(String[] dados, int index) {
        if (index >= 0 && index < dados.length) {
            return dados[index].trim();
        }
        return "";
    }

    private Ativo parseAcao(String linha) {
        String[] dados = linha.split(";");
        String ticker = getField(dados, 0);
        String nome = getField(dados, 1);
        double preco = parseNumber(getField(dados, 2));
        boolean qualificado = "1".equals(getField(dados, 3));
        return new Acao(ticker, nome, preco, qualificado);
    }

    private Ativo parseFII(String linha) {
        String[] dados = linha.split(";");
        String ticker = getField(dados, 0);
        String nome = getField(dados, 1);
        String setor = getField(dados, 2);
        double preco = parseNumber(getField(dados, 3));
        double div = parseNumber(getField(dados, 4));
        double taxa = parseNumber(getField(dados, 5));
        return new FII(ticker, nome, preco, setor, div, taxa);
    }

    private Ativo parseStock(String linha) {
        String[] dados = linha.split(";");
        return new Stock(getField(dados, 0), getField(dados, 1),
                parseNumber(getField(dados, 2)),
                getField(dados, 3), getField(dados, 4), 5.39);
    }

    private Ativo parseCripto(String linha) {
        String[] dados = linha.split(";");
        double qtdMax = dados.length > 4 ? parseNumber(getField(dados, 4)) : 21000000.0;
        return new Criptoativo(getField(dados, 0), getField(dados, 1),
                parseNumber(getField(dados, 2)),
                getField(dados, 3), qtdMax, 5.39);
    }

    private Ativo parseTesouro(String linha) {
        String[] dados = linha.split(";");
        return new Tesouro(getField(dados, 0), getField(dados, 1),
                parseNumber(getField(dados, 2)),
                getField(dados, 3), getField(dados, 4));
    }

    private Investidor parseInvestidor(String linha) {
        String[] dados = linha.split(";");
        if (dados.length < 8)
            return null;

        String tipo = dados[0].trim();
        String nome = dados[1].trim();
        String id = dados[2].trim();
        String tel = dados[3].trim();
        String email = dados[4].trim();
        String endereco = dados[5].trim();
        double patrimonio = Double.parseDouble(dados[6].replace(",", "."));
        String extra = dados[7].trim();

        if (tipo.equalsIgnoreCase("PF")) {
            PerfilInvestidor perfil = PerfilInvestidor.valueOf(extra.toUpperCase());
            return new PessoaFisica(nome, id, tel, email, LocalDate.now(), endereco, patrimonio, perfil);
        } else if (tipo.equalsIgnoreCase("PJ")) {
            return new Institucional(nome, id, tel, email, LocalDate.now(), endereco, patrimonio, extra);
        }
        return null;
    }
}
