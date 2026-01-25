package com.financeiro.service;

import com.financeiro.model.Carteira;
import com.financeiro.model.Ativo;
import com.financeiro.model.Investidor;
import com.financeiro.model.ItemCarteira;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelatorioGerador {

    public String gerarRelatorioTexto(Carteira carteira, Investidor investidor, 
                                     AnaliseService analiseService, 
                                     DiversificacaoService diversificacaoService) {
        
        StringBuilder relatorio = new StringBuilder();
        
        relatorio.append("=".repeat(60)).append("\n");
        relatorio.append("RELATÓRIO DE CARTEIRA DE INVESTIMENTOS\n");
        relatorio.append("=".repeat(60)).append("\n\n");
        
        relatorio.append("INVESTIDOR:\n");
        relatorio.append(String.format("  Nome: %s\n", investidor.getNome()));
        relatorio.append(String.format("  CPF/CNPJ: %s\n", investidor.getIdentificador()));
        relatorio.append(String.format("  Email: %s\n", investidor.getEmail()));
        relatorio.append(String.format("  Telefone: %s\n\n", investidor.getTelefone()));
        
        relatorio.append("CARTEIRA:\n");
        relatorio.append(String.format("  Nome: %s\n", carteira.getNome()));
        relatorio.append(String.format("  Data de Criação: %s\n", carteira.getDataCriacao()));
        relatorio.append(String.format("  Saldo Disponível: R$ %.2f\n\n", carteira.getSaldo()));
        
        relatorio.append("ATIVOS NA CARTEIRA:\n");
        relatorio.append("-".repeat(60)).append("\n");
        relatorio.append(String.format("%-10s %-30s %-8s %-12s %-12s\n", 
            "Ticker", "Nome", "Qtd", "Preço Unit.", "Valor Total"));
        relatorio.append("-".repeat(60)).append("\n");
        
        double valorTotalCarteira = 0;
        // Corrigido para iterar sobre ItemCarteira
        for (Map.Entry<Ativo, com.financeiro.model.ItemCarteira> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            com.financeiro.model.ItemCarteira item = entry.getValue();
            double quantidade = item.getQuantidade(); 
            double valorTotal = quantidade * ativo.getValorEmReais(); // Preço * Fator
            valorTotalCarteira += valorTotal;
            
            relatorio.append(String.format("%-10s %-30s %-8.2f R$ %-10.2f R$ %-10.2f\n",
                ativo.getTicker(), 
                ativo.getNome().length() > 30 ? ativo.getNome().substring(0, 27) + "..." : ativo.getNome(),
                quantidade,
                ativo.getValorEmReais(),
                valorTotal));
        }
        
        relatorio.append("-".repeat(60)).append("\n");
        relatorio.append(String.format("%72s R$ %-10.2f\n", "VALOR TOTAL DA CARTEIRA:", valorTotalCarteira));
        relatorio.append("\n");
        
        double rentabilidadeTotal = analiseService.calcularRentabilidadeTotal(carteira);
        double rentabilidadeAnualizada = analiseService.calcularRentabilidadeAnualizada(carteira);
        
        relatorio.append("DESEMPENHO:\n");
        relatorio.append(String.format("  Rentabilidade Total: %.2f%%\n", rentabilidadeTotal));
        relatorio.append(String.format("  Rentabilidade Anualizada: %.2f%%\n", rentabilidadeAnualizada));
        relatorio.append("\n");
        
        Map<String, Double> desempenhoPorAtivo = analiseService.calcularDesempenhoPorAtivo(carteira);
        relatorio.append("DESEMPENHO POR ATIVO:\n");
        for (Map.Entry<String, Double> entry : desempenhoPorAtivo.entrySet()) {
            relatorio.append(String.format("  %s: %.2f%%\n", entry.getKey(), entry.getValue()));
        }
        relatorio.append("\n");
        
        Map<String, Double> distribuicaoClasses = diversificacaoService.calcularDistribuicaoPorClasse(carteira);
        relatorio.append("DISTRIBUIÇÃO POR CLASSE DE ATIVOS:\n");
        for (Map.Entry<String, Double> entry : distribuicaoClasses.entrySet()) {
            relatorio.append(String.format("  %s: %.2f%%\n", entry.getKey(), entry.getValue()));
        }
        relatorio.append("\n");
        
        Map<String, Double> distribuicaoRenda = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(carteira);
        relatorio.append("DISTRIBUIÇÃO RENDA FIXA vs VARIÁVEL:\n");
        relatorio.append(String.format("  Renda Fixa: %.2f%%\n", distribuicaoRenda.get("Renda Fixa")));
        relatorio.append(String.format("  Renda Variável: %.2f%%\n", distribuicaoRenda.get("Renda Variável")));
        relatorio.append("\n");
        
        Map<String, Double> distribuicaoNacional = diversificacaoService.calcularDistribuicaoNacionalInternacional(carteira);
        relatorio.append("DISTRIBUIÇÃO NACIONAL vs INTERNACIONAL:\n");
        relatorio.append(String.format("  Nacional: %.2f%%\n", distribuicaoNacional.get("Nacional")));
        relatorio.append(String.format("  Internacional: %.2f%%\n", distribuicaoNacional.get("Internacional")));
        relatorio.append("\n");
        
        String avaliacaoDiversificacao = diversificacaoService.avaliarDiversificacao(carteira);
        relatorio.append("AVALIAÇÃO DE DIVERSIFICAÇÃO:\n");
        relatorio.append(String.format("  %s\n", avaliacaoDiversificacao));
        relatorio.append("\n");
        
        relatorio.append("DATA E HORA DA GERAÇÃO: ");
        relatorio.append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
        relatorio.append("\n");
        relatorio.append("=".repeat(60)).append("\n");
        
        return relatorio.toString();
    }

    public void salvarRelatorioArquivo(String relatorio, String nomeArquivo) {
        try (FileWriter writer = new FileWriter(nomeArquivo)) {
            writer.write(relatorio);
            System.out.println("Relatório salvo em: " + nomeArquivo);
        } catch (IOException e) {
            System.out.println("Erro ao salvar relatório: " + e.getMessage());
        }
    }

    public String gerarRelatorioJSON(Carteira carteira, Investidor investidor,
                                   AnaliseService analiseService,
                                   DiversificacaoService diversificacaoService) {
        
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        
        json.append("  \"investidor\": {\n");
        json.append(String.format("    \"nome\": \"%s\",\n", investidor.getNome()));
        json.append(String.format("    \"identificador\": \"%s\",\n", investidor.getIdentificador()));
        json.append(String.format("    \"email\": \"%s\",\n", investidor.getEmail()));
        json.append(String.format("    \"telefone\": \"%s\"\n", investidor.getTelefone()));
        json.append("  },\n");
        
        json.append("  \"carteira\": {\n");
        json.append(String.format("    \"nome\": \"%s\",\n", carteira.getNome()));
        json.append(String.format("    \"dataCriacao\": \"%s\",\n", carteira.getDataCriacao()));
        json.append(String.format("    \"saldoDisponivel\": %.2f,\n", carteira.getSaldo()));
        
        double valorTotalCarteira = 0;
        json.append("    \"ativos\": [\n");
        boolean primeiroAtivo = true;
        for (Map.Entry<Ativo, ItemCarteira> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            ItemCarteira item = entry.getValue();
            double quantidade = item.getQuantidade();
            double valorTotal = quantidade * ativo.getValorEmReais();
            valorTotalCarteira += valorTotal;
            
            if (!primeiroAtivo) {
                json.append(",\n");
            }
            primeiroAtivo = false;
            
            json.append("      {\n");
            json.append(String.format("        \"ticker\": \"%s\",\n", ativo.getTicker()));
            json.append(String.format("        \"nome\": \"%s\",\n", ativo.getNome()));
            json.append(String.format("        \"quantidade\": %.2f,\n", quantidade));
            json.append(String.format("        \"precoUnitario\": %.2f,\n", ativo.getValorEmReais()));
            json.append(String.format("        \"valorGasto\": %.2f,\n", item.getValorTotalGasto()));
            json.append(String.format("        \"valorAtual\": %.2f\n", valorTotal));
            json.append("      }");
        }
        json.append("\n    ],\n");
        json.append(String.format("    \"valorTotalCarteira\": %.2f\n", valorTotalCarteira));
        json.append("  },\n");
        
        double rentabilidadeTotal = analiseService.calcularRentabilidadeTotal(carteira);
        double rentabilidadeAnualizada = analiseService.calcularRentabilidadeAnualizada(carteira);
        
        json.append("  \"desempenho\": {\n");
        json.append(String.format("    \"rentabilidadeTotal\": %.2f,\n", rentabilidadeTotal));
        json.append(String.format("    \"rentabilidadeAnualizada\": %.2f\n", rentabilidadeAnualizada));
        json.append("  },\n");
        
        Map<String, Double> distribuicaoClasses = diversificacaoService.calcularDistribuicaoPorClasse(carteira);
        json.append("  \"distribuicaoClasses\": {\n");
        boolean primeiraClasse = true;
        for (Map.Entry<String, Double> entry : distribuicaoClasses.entrySet()) {
            if (!primeiraClasse) {
                json.append(",\n");
            }
            primeiraClasse = false;
            json.append(String.format("    \"%s\": %.2f", entry.getKey(), entry.getValue()));
        }
        json.append("\n  },\n");
        
        Map<String, Double> distribuicaoRenda = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(carteira);
        json.append("  \"distribuicaoRenda\": {\n");
        json.append(String.format("    \"rendaFixa\": %.2f,\n", distribuicaoRenda.get("Renda Fixa")));
        json.append(String.format("    \"rendaVariavel\": %.2f\n", distribuicaoRenda.get("Renda Variável")));
        json.append("  },\n");
        
        Map<String, Double> distribuicaoNacional = diversificacaoService.calcularDistribuicaoNacionalInternacional(carteira);
        json.append("  \"distribuicaoNacional\": {\n");
        json.append(String.format("    \"nacional\": %.2f,\n", distribuicaoNacional.get("Nacional")));
        json.append(String.format("    \"internacional\": %.2f\n", distribuicaoNacional.get("Internacional")));
        json.append("  },\n");
        
        String avaliacaoDiversificacao = diversificacaoService.avaliarDiversificacao(carteira);
        json.append(String.format("  \"avaliacaoDiversificacao\": \"%s\",\n", avaliacaoDiversificacao));
        
        json.append(String.format("  \"dataGeracao\": \"%s\"\n", 
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))));
        
        json.append("}");
        
        return json.toString();
    }
}
