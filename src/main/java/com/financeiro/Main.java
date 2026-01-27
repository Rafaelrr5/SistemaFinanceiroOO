package com.financeiro;

import com.financeiro.ui.Menu;
import com.financeiro.service.ArquivoService;
import com.financeiro.repository.AtivoRepository;
import com.financeiro.repository.InvestidorRepository;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Inicializando Sistema de Gestão de Carteiras ===");
        
        AtivoRepository ativoRepository = new AtivoRepository();
        InvestidorRepository investidorRepository = new InvestidorRepository();
        

        ArquivoService arquivoService = new ArquivoService(ativoRepository, investidorRepository);
        carregarAtivosIniciais(arquivoService);
        
        System.out.println("Sistema iniciado com sucesso!\n");
        
        Menu menu = new Menu(ativoRepository, investidorRepository);
        menu.exibirMenuPrincipal();
    }
    
    private static void carregarAtivosIniciais(ArquivoService arquivoService) {
        String basePath = "src/main/resources/";
        
        String[][] arquivos = {
            {basePath + "acao.csv", "ACAO"},
            {basePath + "fii.csv", "FII"},
            {basePath + "stock.csv", "STOCK"},
            {basePath + "criptoativo.csv", "CRIPTO"},
            {basePath + "tesouro.csv", "TESOURO"}
        };
        
        for (String[] arquivo : arquivos) {
            try {
                arquivoService.importarAtivos(arquivo[0], arquivo[1]);
                System.out.println("✓ Carregado: " + arquivo[0]);
            } catch (Exception e) {
                System.out.println("⚠ Erro ao carregar " + arquivo[0] + ": " + e.getMessage());
            }
        }
    }
}
