package com.financeiro.ui;

import com.financeiro.model.*;
import com.financeiro.service.*;
import com.financeiro.repository.*;
import java.util.Scanner;
import java.util.List;
import java.util.Map;

public class Menu {
    private Scanner scanner;
    private AtivoRepository ativoRepository;
    private CarteiraRepository carteiraRepository;
    private TransacaoRepository transacaoRepository;
    private CotacaoRepository cotacaoRepository;
    private CarteiraService carteiraService;
    private TransacaoService transacaoService;
    private ValidacaoService validacaoService;
    private AnaliseService analiseService;
    private DiversificacaoService diversificacaoService;
    private ComparativoService comparativoService;
    private RelatorioGerador relatorioGerador;

    public Menu() {
        scanner = new Scanner(System.in);
        ativoRepository = new AtivoRepository();
        carteiraRepository = new CarteiraRepository();
        transacaoRepository = new TransacaoRepository();
        cotacaoRepository = new CotacaoRepository();
        validacaoService = new ValidacaoService();
        transacaoService = new TransacaoService(transacaoRepository, validacaoService);
        carteiraService = new CarteiraService(carteiraRepository);
        analiseService = new AnaliseService(transacaoRepository, cotacaoRepository);
        diversificacaoService = new DiversificacaoService();
        comparativoService = new ComparativoService();
        relatorioGerador = new RelatorioGerador();
    }

    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== SISTEMA DE GESTÃO DE CARTEIRAS ===");
            System.out.println("1. Gerenciar Ativos");
            System.out.println("2. Gerenciar Investidores");
            System.out.println("3. Gerenciar Carteiras");
            System.out.println("4. Análises e Relatórios");
            System.out.println("5. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    menuAtivos();
                    break;
                case 2:
                    menuInvestidores();
                    break;
                case 3:
                    menuCarteiras();
                    break;
                case 4:
                    menuAnalises();
                    break;
                case 5:
                    System.out.println("Saindo do sistema...");
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void menuAtivos() {
        while (true) {
            System.out.println("\n=== GERENCIAR ATIVOS ===");
            System.out.println("1. Listar todos os ativos");
            System.out.println("2. Buscar ativo por ticker");
            System.out.println("3. Listar apenas Ações");
            System.out.println("4. Listar apenas FIIs");
            System.out.println("5. Listar apenas Criptoativos");
            System.out.println("6. Listar apenas Stocks");
            System.out.println("7. Listar apenas Tesouro");
            System.out.println("8. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao == 8)
                break;

            List<Ativo> ativos = ativoRepository.listarTodos();

            switch (opcao) {
                case 1:
                    listarAtivos(ativos);
                    break;
                case 2:
                    buscarAtivoPorTicker();
                    break;
                case 3:
                    listarPorTipo(ativos, "Ação");
                    break;
                case 4:
                    listarPorTipo(ativos, "FII");
                    break;
                case 5:
                    listarPorTipo(ativos, "Criptoativo");
                    break;
                case 6:
                    listarPorTipo(ativos, "Stock");
                    break;
                case 7:
                    listarPorTipo(ativos, "Tesouro");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void listarAtivos(List<Ativo> ativos) {
        System.out.println("\n=== TODOS OS ATIVOS ===");
        System.out.printf("%-10s %-30s %-12s %-20s\n", "Ticker", "Nome", "Preço (R$)", "Tipo");
        System.out.println("-".repeat(75));

        for (Ativo ativo : ativos) {
            String tipo = ativo.getClass().getSimpleName();
            System.out.printf("%-10s %-30s %-12.2f %-20s\n",
                    ativo.getTicker(),
                    ativo.getNome().length() > 30 ? ativo.getNome().substring(0, 27) + "..." : ativo.getNome(),
                    ativo.getPreco(),
                    tipo);
        }
        System.out.println("Total: " + ativos.size() + " ativos");
    }

    private void buscarAtivoPorTicker() {
        System.out.print("Digite o ticker do ativo: ");
        String ticker = scanner.nextLine();

        Ativo ativo = ativoRepository.buscarPorTicker(ticker);
        if (ativo != null) {
            System.out.println("\nAtivo encontrado:");
            System.out.println(ativo);
        } else {
            System.out.println("Ativo não encontrado: " + ticker);
        }
    }

    private void listarPorTipo(List<Ativo> ativos, String tipo) {
        System.out.println("\n=== " + tipo.toUpperCase() + "S ===");
        int count = 0;

        for (Ativo ativo : ativos) {
            if (ativo.getClass().getSimpleName().equals(tipo)) {
                if (count == 0) {
                    System.out.printf("%-10s %-30s %-12s\n", "Ticker", "Nome", "Preço (R$)");
                    System.out.println("-".repeat(55));
                }
                System.out.printf("%-10s %-30s %-12.2f\n",
                        ativo.getTicker(),
                        ativo.getNome().length() > 30 ? ativo.getNome().substring(0, 27) + "..." : ativo.getNome(),
                        ativo.getPreco());
                count++;
            }
        }

        if (count == 0) {
            System.out.println("Nenhum " + tipo.toLowerCase() + " encontrado.");
        } else {
            System.out.println("Total: " + count + " " + tipo.toLowerCase() + "(s)");
        }
    }

    private void menuInvestidores() {
        System.out.println("\n=== GERENCIAR INVESTIDORES ===");
        System.out.println("Funcionalidade em desenvolvimento...");
        System.out.println("Pressione Enter para continuar...");
        scanner.nextLine();
    }

    private void menuCarteiras() {
        while (true) {
            System.out.println("\n=== GERENCIAR CARTEIRAS ===");
            System.out.println("1. Criar nova carteira");
            System.out.println("2. Listar carteiras");
            System.out.println("3. Selecionar carteira");
            System.out.println("4. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    criarCarteira();
                    break;
                case 2:
                    listarCarteiras();
                    break;
                case 3:
                    selecionarCarteira();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void criarCarteira() {
        System.out.print("Digite o nome da nova carteira: ");
        String nome = scanner.nextLine();

        Carteira novaCarteira = new Carteira(nome);
        carteiraService.criarCarteira(novaCarteira);

        System.out.print("Deseja adicionar saldo inicial? (s/n): ");
        String resposta = scanner.nextLine();

        if (resposta.equalsIgnoreCase("s")) {
            System.out.print("Digite o valor do saldo inicial: R$ ");
            double saldo = scanner.nextDouble();
            scanner.nextLine();

            carteiraService.adicionarSaldo(novaCarteira, saldo);
        }

        System.out.println("Carteira '" + nome + "' criada com sucesso!");
    }

    private void listarCarteiras() {
        List<Carteira> carteiras = carteiraService.listarCarteiras();

        if (carteiras.isEmpty()) {
            System.out.println("Nenhuma carteira cadastrada.");
            return;
        }

        System.out.println("\n=== CARTEIRAS CADASTRADAS ===");
        for (Carteira carteira : carteiras) {
            System.out.println("Nome: " + carteira.getNome());
            System.out.println("Data de Criação: " + carteira.getDataCriacao());
            System.out.println("Saldo: R$ " + carteira.getSaldo());
            System.out.println("Quantidade de ativos: " + carteira.getAtivos().size());
            System.out.println("-".repeat(40));
        }
    }

    private void selecionarCarteira() {
        System.out.print("Digite o nome da carteira: ");
        String nome = scanner.nextLine();

        try {
            Carteira carteira = carteiraService.buscarCarteira(nome);
            menuCarteiraSelecionada(carteira);
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuCarteiraSelecionada(Carteira carteira) {
        while (true) {
            System.out.println("\n=== CARTEIRA: " + carteira.getNome().toUpperCase() + " ===");
            System.out.println("Saldo disponível: R$ " + carteira.getSaldo());
            System.out.println("Quantidade de ativos: " + carteira.getAtivos().size());
            System.out.println("\n1. Comprar ativo");
            System.out.println("2. Vender ativo");
            System.out.println("3. Listar ativos da carteira");
            System.out.println("4. Adicionar saldo");
            System.out.println("5. Ver transações");
            System.out.println("6. Voltar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    comprarAtivo(carteira);
                    break;
                case 2:
                    venderAtivo(carteira);
                    break;
                case 3:
                    listarAtivosCarteira(carteira);
                    break;
                case 4:
                    adicionarSaldo(carteira);
                    break;
                case 5:
                    verTransacoes();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void comprarAtivo(Carteira carteira) {
        System.out.print("Digite o ticker do ativo: ");
        String ticker = scanner.nextLine();

        Ativo ativo = ativoRepository.buscarPorTicker(ticker);
        if (ativo == null) {
            System.out.println("Ativo não encontrado: " + ticker);
            return;
        }

        System.out.print("Digite a quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Digite o preço unitário: R$ ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        try {
            transacaoService.comprar(carteira, ativo, quantidade, preco);
            System.out.println("Compra realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro na compra: " + e.getMessage());
        }
    }

    private void venderAtivo(Carteira carteira) {
        System.out.print("Digite o ticker do ativo: ");
        String ticker = scanner.nextLine();

        Ativo ativo = ativoRepository.buscarPorTicker(ticker);
        if (ativo == null) {
            System.out.println("Ativo não encontrado: " + ticker);
            return;
        }

        System.out.print("Digite a quantidade: ");
        int quantidade = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Digite o preço unitário: R$ ");
        double preco = scanner.nextDouble();
        scanner.nextLine();

        try {
            transacaoService.vender(carteira, ativo, quantidade, preco);
            System.out.println("Venda realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro na venda: " + e.getMessage());
        }
    }

    private void listarAtivosCarteira(Carteira carteira) {
        System.out.println("\n=== ATIVOS NA CARTEIRA ===");
        System.out.printf("%-10s %-30s %-8s %-12s %-12s\n",
                "Ticker", "Nome", "Qtd", "Preço Unit.", "Valor Total");
        System.out.println("-".repeat(75));

        double valorTotal = 0;
        for (Map.Entry<Ativo, Integer> entry : carteira.getAtivos().entrySet()) {
            Ativo ativo = entry.getKey();
            int quantidade = entry.getValue();
            double valor = quantidade * ativo.getPreco();
            valorTotal += valor;

            System.out.printf("%-10s %-30s %-8d R$ %-10.2f R$ %-10.2f\n",
                    ativo.getTicker(),
                    ativo.getNome().length() > 30 ? ativo.getNome().substring(0, 27) + "..." : ativo.getNome(),
                    quantidade,
                    ativo.getPreco(),
                    valor);
        }

        System.out.println("-".repeat(75));
        System.out.printf("%60s R$ %-10.2f\n", "VALOR TOTAL:", valorTotal);
    }

    private void adicionarSaldo(Carteira carteira) {
        System.out.print("Digite o valor a adicionar: R$ ");
        double valor = scanner.nextDouble();
        scanner.nextLine();

        carteiraService.adicionarSaldo(carteira, valor);
        System.out.println("Saldo adicionado com sucesso!");
        System.out.println("Novo saldo: R$ " + carteira.getSaldo());
    }

    private void verTransacoes() {
        List<Transacao> transacoes = transacaoService.listarTransacoes();

        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma transação registrada.");
            return;
        }

        System.out.println("\n=== HISTÓRICO DE TRANSAÇÕES ===");
        for (Transacao transacao : transacoes) {
            System.out.println(transacao);
        }
    }

    private void menuAnalises() {
        while (true) {
            System.out.println("\n=== ANÁLISES E RELATÓRIOS ===");
            System.out.println("1. Análise de rentabilidade");
            System.out.println("2. Análise de diversificação");
            System.out.println("3. Comparativo entre carteiras");
            System.out.println("4. Gerar relatório completo");
            System.out.println("5. Calculadora financeira");
            System.out.println("6. Voltar ao menu principal");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    menuRentabilidade();
                    break;
                case 2:
                    menuDiversificacao();
                    break;
                case 3:
                    menuComparativo();
                    break;
                case 4:
                    menuRelatorio();
                    break;
                case 5:
                    menuCalculadora();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void menuRentabilidade() {
        System.out.print("Digite o nome da carteira: ");
        String nome = scanner.nextLine();

        try {
            Carteira carteira = carteiraService.buscarCarteira(nome);

            double rentabilidadeTotal = analiseService.calcularRentabilidadeTotal(carteira);
            double rentabilidadeAnualizada = analiseService.calcularRentabilidadeAnualizada(carteira);

            System.out.println("\n=== ANÁLISE DE RENTABILIDADE ===");
            System.out.println("Carteira: " + carteira.getNome());
            System.out.println("Rentabilidade Total: " + String.format("%.2f", rentabilidadeTotal) + "%");
            System.out.println("Rentabilidade Anualizada: " + String.format("%.2f", rentabilidadeAnualizada) + "%");

            Map<String, Double> desempenhoPorAtivo = analiseService.calcularDesempenhoPorAtivo(carteira);
            System.out.println("\nDesempenho por ativo:");
            for (Map.Entry<String, Double> entry : desempenhoPorAtivo.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + String.format("%.2f", entry.getValue()) + "%");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuDiversificacao() {
        System.out.print("Digite o nome da carteira: ");
        String nome = scanner.nextLine();

        try {
            Carteira carteira = carteiraService.buscarCarteira(nome);

            System.out.println("\n=== ANÁLISE DE DIVERSIFICAÇÃO ===");
            System.out.println("Carteira: " + carteira.getNome());

            Map<String, Double> distribuicaoClasses = diversificacaoService.calcularDistribuicaoPorClasse(carteira);
            System.out.println("\nDistribuição por classe:");
            for (Map.Entry<String, Double> entry : distribuicaoClasses.entrySet()) {
                System.out.println("  " + entry.getKey() + ": " + String.format("%.2f", entry.getValue()) + "%");
            }

            Map<String, Double> distribuicaoRenda = diversificacaoService
                    .calcularDistribuicaoRendaFixaVariavel(carteira);
            System.out.println("\nDistribuição Renda Fixa vs Variável:");
            System.out.println("  Renda Fixa: " + String.format("%.2f", distribuicaoRenda.get("Renda Fixa")) + "%");
            System.out.println(
                    "  Renda Variável: " + String.format("%.2f", distribuicaoRenda.get("Renda Variável")) + "%");

            Map<String, Double> distribuicaoNacional = diversificacaoService
                    .calcularDistribuicaoNacionalInternacional(carteira);
            System.out.println("\nDistribuição Nacional vs Internacional:");
            System.out.println("  Nacional: " + String.format("%.2f", distribuicaoNacional.get("Nacional")) + "%");
            System.out.println(
                    "  Internacional: " + String.format("%.2f", distribuicaoNacional.get("Internacional")) + "%");

            String avaliacao = diversificacaoService.avaliarDiversificacao(carteira);
            System.out.println("\nAvaliação: " + avaliacao);

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuComparativo() {
        List<Carteira> carteiras = carteiraService.listarCarteiras();

        if (carteiras.size() < 2) {
            System.out.println("É necessário ter pelo menos 2 carteiras para comparar.");
            return;
        }

        System.out.println("\n=== COMPARATIVO ENTRE CARTEIRAS ===");

        Map<String, Double> comparativoValor = comparativoService.compararCarteiras(carteiras);
        System.out.println("\nValor total das carteiras:");
        for (Map.Entry<String, Double> entry : comparativoValor.entrySet()) {
            System.out.println("  " + entry.getKey() + ": R$ " + String.format("%.2f", entry.getValue()));
        }

        Map<String, Double> comparativoRentabilidade = comparativoService.compararRentabilidade(carteiras,
                analiseService);
        System.out.println("\nRentabilidade total:");
        for (Map.Entry<String, Double> entry : comparativoRentabilidade.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + String.format("%.2f", entry.getValue()) + "%");
        }

        Map<String, String> comparativoDiversificacao = comparativoService.compararDiversificacao(carteiras,
                diversificacaoService);
        System.out.println("\nAvaliação de diversificação:");
        for (Map.Entry<String, String> entry : comparativoDiversificacao.entrySet()) {
            System.out.println("  " + entry.getKey() + ": " + entry.getValue());
        }

        Carteira melhorCarteira = comparativoService.identificarMelhorCarteira(carteiras, analiseService);
        System.out.println("\nMelhor carteira por rentabilidade: " +
                (melhorCarteira != null ? melhorCarteira.getNome() : "N/A"));
    }

    private void menuRelatorio() {
        System.out.print("Digite o nome da carteira: ");
        String nome = scanner.nextLine();

        try {
            Carteira carteira = carteiraService.buscarCarteira(nome);

            System.out.println("\n=== GERAR RELATÓRIO ===");
            System.out.println("1. Relatório em texto");
            System.out.println("2. Relatório em JSON");
            System.out.print("Escolha o formato: ");

            int formato = scanner.nextInt();
            scanner.nextLine();

            Investidor investidorExemplo = new Investidor("João Silva", "123.456.789-00", "joao@email.com",
                    "(11) 99999-9999");

            switch (formato) {
                case 1:
                    String relatorioTexto = relatorioGerador.gerarRelatorioTexto(carteira, investidorExemplo,
                            analiseService, diversificacaoService);
                    System.out.println(relatorioTexto);

                    System.out.print("Deseja salvar em arquivo? (s/n): ");
                    String salvar = scanner.nextLine();
                    if (salvar.equalsIgnoreCase("s")) {
                        System.out.print("Digite o nome do arquivo: ");
                        String nomeArquivo = scanner.nextLine();
                        relatorioGerador.salvarRelatorioArquivo(relatorioTexto, nomeArquivo + ".txt");
                    }
                    break;

                case 2:
                    String relatorioJSON = relatorioGerador.gerarRelatorioJSON(carteira, investidorExemplo,
                            analiseService, diversificacaoService);
                    System.out.println(relatorioJSON);

                    System.out.print("Deseja salvar em arquivo? (s/n): ");
                    salvar = scanner.nextLine();
                    if (salvar.equalsIgnoreCase("s")) {
                        System.out.print("Digite o nome do arquivo: ");
                        String nomeArquivo = scanner.nextLine();
                        relatorioGerador.salvarRelatorioArquivo(relatorioJSON, nomeArquivo + ".json");
                    }
                    break;

                default:
                    System.out.println("Opção inválida!");
            }

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void menuCalculadora() {
        while (true) {
            System.out.println("\n=== CALCULADORA FINANCEIRA ===");
            System.out.println("1. Calcular variação percentual");
            System.out.println("2. Calcular juros simples");
            System.out.println("3. Calcular juros compostos");
            System.out.println("4. Calcular valor futuro");
            System.out.println("5. Voltar ao menu anterior");
            System.out.print("Escolha uma opção: ");

            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    calcularVariacaoPercentual();
                    break;
                case 2:
                    calcularJurosSimples();
                    break;
                case 3:
                    calcularJurosCompostos();
                    break;
                case 4:
                    calcularValorFuturo();
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Opção inválida!");
            }
        }
    }

    private void calcularVariacaoPercentual() {
        System.out.print("Digite o valor inicial: R$ ");
        double inicial = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite o valor final: R$ ");
        double finalValor = scanner.nextDouble();
        scanner.nextLine();

        double variacao = CalculadoraFinanceira.calcularVariacaoPercentual(inicial, finalValor);
        System.out.println("Variação percentual: " + String.format("%.2f", variacao) + "%");
    }

    private void calcularJurosSimples() {
        System.out.print("Digite o valor principal: R$ ");
        double principal = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite a taxa (em decimal, ex: 0.05 para 5%): ");
        double taxa = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite o número de períodos: ");
        int periodos = scanner.nextInt();
        scanner.nextLine();

        double juros = CalculadoraFinanceira.calcularJurosSimples(principal, taxa, periodos);
        System.out.println("Juros simples: R$ " + String.format("%.2f", juros));
        System.out.println("Montante final: R$ " + String.format("%.2f", principal + juros));
    }

    private void calcularJurosCompostos() {
        System.out.print("Digite o valor principal: R$ ");
        double principal = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite a taxa (em decimal, ex: 0.05 para 5%): ");
        double taxa = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite o número de períodos: ");
        int periodos = scanner.nextInt();
        scanner.nextLine();

        double montante = CalculadoraFinanceira.calcularJurosCompostos(principal, taxa, periodos);
        System.out.println("Montante com juros compostos: R$ " + String.format("%.2f", montante));
        System.out.println("Juros totais: R$ " + String.format("%.2f", montante - principal));
    }

    private void calcularValorFuturo() {
        System.out.print("Digite o aporte mensal: R$ ");
        double aporte = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite a taxa mensal (em decimal, ex: 0.01 para 1%): ");
        double taxa = scanner.nextDouble();
        scanner.nextLine();

        System.out.print("Digite o número de meses: ");
        int meses = scanner.nextInt();
        scanner.nextLine();

        double valorFuturo = CalculadoraFinanceira.calcularValorFuturo(aporte, taxa, meses);
        System.out.println("Valor futuro: R$ " + String.format("%.2f", valorFuturo));
        System.out.println("Total investido: R$ " + String.format("%.2f", aporte * meses));
        System.out.println("Juros acumulados: R$ " + String.format("%.2f", valorFuturo - (aporte * meses)));
    }
}