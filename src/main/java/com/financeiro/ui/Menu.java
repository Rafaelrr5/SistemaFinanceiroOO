package com.financeiro.ui;

import com.financeiro.model.*;
import com.financeiro.service.*;
import com.financeiro.repository.*;
import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

public class Menu {

    /**
     * Lê um inteiro do usuário com tratamento de erro.
     * Continua pedindo até receber um valor válido.
     */
    private int lerInteiro(String prompt) {
        while (true) {
            try {
                if (prompt != null && !prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Entrada inválida! Digite um número inteiro.");
            }
        }
    }

    /**
     * Lê um inteiro com valor padrão em caso de entrada vazia.
     */
    private int lerInteiroOpcional(String prompt, int padrao) {
        if (prompt != null && !prompt.isEmpty()) {
            System.out.print(prompt);
        }
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return padrao;
        }
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("⚠️  Entrada inválida! Usando valor padrão: " + padrao);
            return padrao;
        }
    }

    /**
     * Lê um número decimal do usuário com tratamento de erro.
     */
    private double lerDouble(String prompt) {
        while (true) {
            try {
                if (prompt != null && !prompt.isEmpty()) {
                    System.out.print(prompt);
                }
                String input = scanner.nextLine().trim().replace(",", ".");
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Entrada inválida! Digite um número válido.");
            }
        }
    }

    /**
     * Lê um booleano do usuário com tratamento de erro.
     */
    private boolean lerBoolean(String prompt) {
        while (true) {
            if (prompt != null && !prompt.isEmpty()) {
                System.out.print(prompt);
            }
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("true") || input.equals("sim") || input.equals("s") || input.equals("1")) {
                return true;
            } else if (input.equals("false") || input.equals("nao") || input.equals("não") || input.equals("n")
                    || input.equals("0")) {
                return false;
            }
            System.out.println("⚠️  Entrada inválida! Digite sim/nao ou true/false.");
        }
    }

    private Scanner scanner;
    private AtivoRepository ativoRepository;
    private CarteiraRepository carteiraRepository;
    private TransacaoRepository transacaoRepository;
    private InvestidorRepository investidorRepository;
    private CotacaoRepository cotacaoRepository;

    private CarteiraService carteiraService;
    private TransacaoService transacaoService;
    private ValidacaoService validacaoService;
    private AnaliseService analiseService;
    private DiversificacaoService diversificacaoService;
    private ComparativoService comparativoService;
    private RelatorioGerador relatorioGerador;
    private InvestidorService investidorService;
    private AtivoService ativoService;
    private ArquivoService arquivoService;

    public Menu() {
        this(new AtivoRepository(), new InvestidorRepository());
    }

    // Construtor que recebe repositórios já carregados
    public Menu(AtivoRepository ativoRepository, InvestidorRepository investidorRepository) {
        scanner = new Scanner(System.in);
        this.ativoRepository = ativoRepository;
        this.investidorRepository = investidorRepository;

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

        investidorService = new InvestidorService(investidorRepository);
        ativoService = new AtivoService(ativoRepository, investidorRepository);
        arquivoService = new ArquivoService(ativoRepository, investidorRepository);
    }

    public void exibirMenuPrincipal() {
        while (true) {
            System.out.println("\n=== SISTEMA DE GESTÃO DE CARTEIRAS ===");
            System.out.println("1. Ativos");
            System.out.println("2. Investidores");
            System.out.println("3. Sair");

            int opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    menuAtivos();
                    break;
                case 2:
                    menuInvestidores();
                    break;
                case 3:
                    System.out.println("Saindo do sistema...");
                    return;
                default:
                    System.out.println("⚠️  Opção inválida! Escolha entre 1 e 3.");
            }
        }
    }

    private void menuAtivos() {
        while (true) {
            System.out.println("\n=== MENU ATIVOS ===");
            System.out.println("1. Cadastrar ativo");
            System.out.println("2. Cadastrar ativo em lote");
            System.out.println("3. Editar ativo");
            System.out.println("4. Excluir ativo");
            System.out.println("5. Exibir relatório de ativos");
            System.out.println("6. Voltar");

            int opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    cadastrarAtivo();
                    break;
                case 2:
                    cadastrarAtivoLote();
                    break;
                case 3:
                    editarAtivo();
                    break;
                case 4:
                    excluirAtivo();
                    break;
                case 5:
                    submenuRelatorioAtivos();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("⚠️  Opção inválida! Escolha entre 1 e 6.");
            }
        }
    }

    private void submenuRelatorioAtivos() {
        System.out.println("\n--- Relatório de Ativos ---");
        System.out.println("1. Todos os ativos");
        System.out.println("2. Apenas Ações");
        System.out.println("3. Apenas FIIs");
        System.out.println("4. Apenas Criptoativos");
        System.out.println("5. Apenas Stocks");
        System.out.println("6. Apenas Tesouro");

        int op = lerInteiro("Escolha: ");

        List<Ativo> ativos = ativoRepository.listarTodos();
        switch (op) {
            case 1:
                listarAtivos(ativos);
                break;
            case 2:
                listarPorTipo(ativos, "Acao");
                break; // SimpleName é Acao sem acento
            case 3:
                listarPorTipo(ativos, "FII");
                break;
            case 4:
                listarPorTipo(ativos, "Criptoativo");
                break;
            case 5:
                listarPorTipo(ativos, "Stock");
                break;
            case 6:
                listarPorTipo(ativos, "Tesouro");
                break;
            default:
                System.out.println("⚠️  Opção inválida! Escolha entre 1 e 6.");
        }
    }

    private void cadastrarAtivo() {
        System.out.println("Tipos: 1-Ação, 2-FII, 3-Stock, 4-Tesouro, 5-Cripto");
        int tipo = lerInteiro("Escolha o tipo: ");

        if (tipo < 1 || tipo > 5) {
            System.out.println("⚠️  Tipo inválido! Escolha entre 1 e 5.");
            return;
        }

        try {
            System.out.print("Ticker: ");
            String ticker = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            double preco = lerDouble("Preço: ");

            Ativo novo = null;
            if (tipo == 1) {
                boolean q = lerBoolean("Qualificado (sim/nao): ");
                novo = new Acao(ticker, nome, preco, q);
            } else if (tipo == 2) {
                System.out.print("Setor: ");
                String setor = scanner.nextLine();
                double div = lerDouble("Ultimo Dividendo: ");
                double taxa = lerDouble("Taxa Admin: ");
                novo = new FII(ticker, nome, preco, setor, div, taxa);
            } else if (tipo == 3) { // Stock
                System.out.print("Bolsa: ");
                String bolsa = scanner.nextLine();
                System.out.print("Setor: ");
                String setor = scanner.nextLine();
                novo = new Stock(ticker, nome, preco, bolsa, setor, 5.39);
            } else if (tipo == 4) { // Tesouro
                System.out.print("Tipo Rend.: ");
                String rend = scanner.nextLine();
                System.out.print("Vencimento: ");
                String venc = scanner.nextLine();
                novo = new Tesouro(ticker, nome, preco, rend, venc);
            } else { // Cripto
                System.out.print("Algoritmo: ");
                String algo = scanner.nextLine();
                double max = lerDouble("Qtd Max: ");
                novo = new Criptoativo(ticker, nome, preco, algo, max, 5.39);
            }

            ativoService.cadastrar(novo);
            System.out.println("✅ Ativo cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao cadastrar: "
                    + (e.getMessage() != null ? e.getMessage() : "Verifique os dados informados."));
        }
    }

    private void cadastrarAtivoLote() {
        System.out.println("Tipo arquivo: ACAO, FII, STOCK, TESOURO, CRIPTO");
        String tipo = scanner.nextLine();
        System.out.print("Caminho arquivo: ");
        String path = scanner.nextLine();
        try {
            arquivoService.importarAtivos(path, tipo);
            System.out.println("Importado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void editarAtivo() {
        System.out.print("Ticker do ativo a editar: ");
        String ticker = scanner.nextLine();
        Ativo a = ativoRepository.buscarPorTicker(ticker);
        if (a != null) {
            System.out.println("Ativo encontrado: " + a.getNome() + " - Preço: " + a.getPreco());
            System.out.print("Novo nome (Enter para manter): ");
            String novoNome = scanner.nextLine();
            if (!novoNome.isEmpty()) {
                a.setNome(novoNome);
            }

            System.out.print("Novo preço (0 para manter): ");
            String precoInput = scanner.nextLine().trim().replace(",", ".");
            if (!precoInput.isEmpty()) {
                try {
                    double novoPreco = Double.parseDouble(precoInput);
                    if (novoPreco > 0) {
                        a.setPreco(novoPreco);
                    } else if (novoPreco < 0) {
                        System.out.println("⚠️  Preço inválido (negativo). Mantido o anterior.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("⚠️  Formato de preço inválido. Mantido o anterior.");
                }
            }

            System.out.println("✅ Ativo atualizado com sucesso!");
        } else {
            System.out.println("❌ Ativo não encontrado com ticker: " + ticker);
        }
    }

    private void excluirAtivo() {
        System.out.print("Ticker: ");
        String ticker = scanner.nextLine();
        ativoService.excluir(ticker);
        System.out.println("Excluido (se existia).");
    }

    private void menuInvestidores() {
        while (true) {
            System.out.println("\n=== MENU INVESTIDORES ===");
            System.out.println("1. Cadastrar investidor");
            System.out.println("2. Cadastrar investidor em lote");
            System.out.println("3. Exibir todos investidores");
            System.out.println("4. Excluir investidores (lista de IDs)");
            System.out.println("5. Selecionar Investidor (Login)");
            System.out.println("6. Voltar");

            int op = lerInteiro("Escolha: ");

            switch (op) {
                case 1:
                    cadastrarInvestidor();
                    break;
                case 2:
                    cadastrarInvestidorLote();
                    break;
                case 3:
                    exibirTodosInvestidores();
                    break;
                case 4:
                    excluirInvestidoresLote();
                    break;
                case 5:
                    selecionarInvestidor();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("⚠️  Opção inválida! Escolha entre 1 e 6.");
            }
        }
    }

    private void cadastrarInvestidor() {
        System.out.println("1-Pessoa Física (PF), 2-Institucional (PJ)");
        int t = lerInteiro("Escolha o tipo: ");

        if (t < 1 || t > 2) {
            System.out.println("⚠️  Tipo inválido! Escolha 1 ou 2.");
            return;
        }

        try {
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            if (nome == null || nome.trim().isEmpty()) {
                throw new com.financeiro.exception.RegraNegocioException("Nome não pode ser vazio.");
            }

            System.out.print("ID (CPF/CNPJ): ");
            String id = scanner.nextLine();
            if (id == null || id.trim().isEmpty()) {
                throw new com.financeiro.exception.RegraNegocioException("CPF/CNPJ não pode ser vazio.");
            }
            // Valida formato do CPF/CNPJ
            com.financeiro.service.DocumentoValidator.validarDocumento(id);

            System.out.print("Telefone: ");
            String tel = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Data de Nascimento (dd/MM/yyyy): ");
            String dataNascStr = scanner.nextLine();
            LocalDate dataNasc;
            try {
                dataNasc = LocalDate.parse(dataNascStr, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            } catch (Exception e) {
                System.out.println("⚠️  Data inválida. Usando data atual.");
                dataNasc = LocalDate.now();
            }
            System.out.print("Endereço completo: ");
            String end = scanner.nextLine();
            double pat = lerDouble("Patrimonio: ");

            if (pat < 0) {
                throw new com.financeiro.exception.RegraNegocioException("Patrimônio não pode ser negativo.");
            }

            Investidor novo;
            if (t == 1) {
                System.out.println("Perfil: 1-CONSERVADOR, 2-MODERADO, 3-ARROJADO");
                int p = lerInteiro("Escolha o perfil: ");
                if (p < 1 || p > 3) {
                    System.out.println("⚠️  Perfil inválido! Usando MODERADO como padrão.");
                    p = 2;
                }
                PerfilInvestidor perfil = PerfilInvestidor.values()[p - 1];
                novo = new PessoaFisica(nome, id, tel, email, dataNasc, end, pat, perfil);
            } else {
                System.out.print("Razão Social: ");
                String razao = scanner.nextLine();
                novo = new Institucional(nome, id, tel, email, dataNasc, end, pat, razao);
            }
            investidorService.cadastrar(novo);
            System.out.println("✅ Investidor cadastrado com sucesso!");
        } catch (Exception e) {
            System.out
                    .println("❌ Erro: " + (e.getMessage() != null ? e.getMessage() : "Verifique os dados informados."));
        }
    }

    private void cadastrarInvestidorLote() {
        System.out.print("Caminho arquivo: ");
        String path = scanner.nextLine();
        try {
            arquivoService.importarInvestidores(path);
            System.out.println("Importado.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private void exibirTodosInvestidores() {
        List<Investidor> lista = investidorService.listarTodos();
        for (Investidor i : lista) {
            System.out.println(i.getNome() + " - " + i.getIdentificador() + " - " + i.getPatrimonio());
        }
    }

    private void excluirInvestidoresLote() {
        System.out.print("Lista de CPF/CNPJ (separados por vírgula): ");
        String linha = scanner.nextLine();
        String[] ids = linha.split(",");
        investidorService.excluirEmLote(java.util.Arrays.asList(ids));
        System.out.println("Exclusão processada.");
    }

    private void selecionarInvestidor() {
        System.out.print("Digite CPF/CNPJ: ");
        String id = scanner.nextLine();
        Investidor inv = investidorService.buscarPorId(id);
        if (inv != null) {
            menuInvestidorSelecionado(inv);
        } else {
            System.out.println("Não encontrado.");
        }
    }

    private void menuInvestidorSelecionado(Investidor investidor) {
        while (true) {
            System.out.println("\n=== INVESTIDOR: " + investidor.getNome() + " ===");
            System.out.println("1. Editar Informações");
            System.out.println("2. Excluir este investidor");
            System.out.println("3. Exibir ativos do investidor (Tabela)");
            System.out.println("4. Exibir valor total gasto (em Real)");
            System.out.println("5. Exibir valor total atual (em Real)");
            System.out.println("6. Exibir porcentagens renda fixa/variável");
            System.out.println("7. Exibir porcentagens nacional/internacional");
            System.out.println("8. Salvar Relatório (JSON)");
            System.out.println("9. Adicionar movimentação de compra");
            System.out.println("10. Adicionar movimentação de venda");
            System.out.println("11. Adicionar lote de movimentações");
            System.out.println("0. Voltar");

            int op = lerInteiro("Opção: ");

            switch (op) {
                case 1:
                    editarInvestidor(investidor);
                    break;
                case 2:
                    investidorService.excluir(investidor.getIdentificador());
                    System.out.println("✅ Investidor excluído.");
                    return;
                case 3:
                    listarAtivosCarteira(investidor.getCarteira());
                    break;
                case 4:
                    exibirValorTotalGasto(investidor.getCarteira());
                    break;
                case 5:
                    exibirValorTotalAtual(investidor.getCarteira());
                    break;
                case 6:
                    exibirPercentuaisRendaFixaVariavel(investidor.getCarteira());
                    break;
                case 7:
                    exibirPercentuaisNacionalInternacional(investidor.getCarteira());
                    break;
                case 8:
                    salvarRelatorioJson(investidor);
                    break;
                case 9:
                    comprarAtivo(investidor.getCarteira(), investidor);
                    break;
                case 10:
                    venderAtivo(investidor.getCarteira(), investidor);
                    break;
                case 11:
                    System.out.print("Caminho arquivo: ");
                    String path = scanner.nextLine();
                    try {
                        arquivoService.importarTransacoes(path, investidor, transacaoService);
                        System.out.println("✅ Importação concluída.");
                    } catch (Exception e) {
                        System.out.println("❌ Erro: " + e.getMessage());
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("⚠️  Opção inválida! Escolha entre 0 e 11.");
            }
        }
    }

    // Métodos auxiliares para investidor selecionado
    private void editarInvestidor(Investidor investidor) {
        System.out.print("Novo nome (" + investidor.getNome() + ") [Enter para manter]: ");
        String n = scanner.nextLine();
        if (!n.isEmpty())
            investidor.setNome(n);

        System.out.print("Novo patrimonio (0 para manter): ");
        String patInput = scanner.nextLine().trim().replace(",", ".");
        if (!patInput.isEmpty()) {
            try {
                double p = Double.parseDouble(patInput);
                if (p >= 0)
                    investidor.setPatrimonio(p);
                else
                    System.out.println("⚠️  Patrimonio inválido ignorado.");
            } catch (NumberFormatException e) {
                System.out.println("⚠️  Formato inválido. Mantido o anterior.");
            }
        }

        System.out.println("✅ Dados atualizados.");
    }

    /**
     * Exibe tabela de ativos da carteira com: ticker, quantidade, valor gasto e
     * valor atual
     */
    private void listarAtivosCarteira(Carteira carteira) {
        Map<Ativo, ItemCarteira> ativos = carteira.getAtivos();

        if (ativos.isEmpty()) {
            System.out.println("\nCarteira vazia - nenhum ativo encontrado.");
            return;
        }

        System.out.println("\n=== ATIVOS DA CARTEIRA ===");
        System.out.printf("%-12s %-8s %-15s %-15s\n", "Ticker", "Qtd", "Valor Gasto(R$)", "Valor Atual(R$)");
        System.out.println("-".repeat(55));

        for (Map.Entry<Ativo, ItemCarteira> entry : ativos.entrySet()) {
            Ativo ativo = entry.getKey();
            ItemCarteira item = entry.getValue();

            double valorGasto = item.getValorTotalGasto(); // qtd * precoMedioCompra
            double valorAtual = item.getValorTotalAtual(); // qtd * precoAtual

            System.out.printf("%-12s %-8.2f %-15.2f %-15.2f\n",
                    ativo.getTicker(),
                    item.getQuantidade(),
                    valorGasto,
                    valorAtual);
        }
        System.out.println("-".repeat(55));
    }

    /**
     * Exibe o valor total gasto (quantidade x preço médio de compra) em Real.
     */
    private void exibirValorTotalGasto(Carteira c) {
        double custo = c.getAtivos().values().stream()
                .mapToDouble(com.financeiro.model.ItemCarteira::getValorTotalGasto)
                .sum();
        System.out.printf("\nValor Total Gasto: R$ %.2f\n", custo);
    }

    /**
     * Exibe o valor total atual (quantidade x preço atual) em Real.
     */
    private void exibirValorTotalAtual(Carteira c) {
        double atual = c.getValorTotalCarteira();
        System.out.printf("\nValor Total Atual: R$ %.2f\n", atual);
    }

    /**
     * Exibe as porcentagens de produtos de renda fixa e renda variável.
     */
    private void exibirPercentuaisRendaFixaVariavel(Carteira c) {
        Map<String, Double> renda = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(c);
        System.out.println("\n=== DISTRIBUIÇÃO RENDA FIXA / VARIÁVEL ===");
        System.out.printf("Renda Fixa: %.2f%%\n", renda.get("Renda Fixa"));
        System.out.printf("Renda Variável: %.2f%%\n", renda.get("Renda Variável"));
    }

    /**
     * Exibe as porcentagens de produtos nacionais e internacionais.
     */
    private void exibirPercentuaisNacionalInternacional(Carteira c) {
        Map<String, Double> nac = diversificacaoService.calcularDistribuicaoNacionalInternacional(c);
        System.out.println("\n=== DISTRIBUIÇÃO NACIONAL / INTERNACIONAL ===");
        System.out.printf("Nacional: %.2f%%\n", nac.get("Nacional"));
        System.out.printf("Internacional: %.2f%%\n", nac.get("Internacional"));
    }

    // Métodos legados mantidos para compatibilidade com outras partes do código
    private void exibirTotais(Carteira c) {
        double atual = c.getValorTotalCarteira();
        // Custo total seria somar quantidade * precoMedio de todos items
        double custo = c.getAtivos().values().stream()
                .mapToDouble(com.financeiro.model.ItemCarteira::getValorTotalGasto)
                .sum();

        System.out.printf("Total Gasto: R$ %.2f\n", custo);
        System.out.printf("Total Atual: R$ %.2f\n", atual);
    }

    private void exibirPercentuais(Carteira c) {
        Map<String, Double> renda = diversificacaoService.calcularDistribuicaoRendaFixaVariavel(c);
        System.out.println("Renda Fixa: " + renda.get("Renda Fixa") + "%");
        System.out.println("Variável: " + renda.get("Renda Variável") + "%");

        Map<String, Double> nac = diversificacaoService.calcularDistribuicaoNacionalInternacional(c);
        System.out.println("Nacional: " + nac.get("Nacional") + "%");
        System.out.println("Internacional: " + nac.get("Internacional") + "%");
    }

    private void salvarRelatorioJson(Investidor investidor) {
        String json = relatorioGerador.gerarRelatorioJSON(investidor.getCarteira(), investidor, analiseService,
                diversificacaoService);
        System.out.println(json);
        // Em um sistema real salvaria em arquivo
        relatorioGerador.salvarRelatorioArquivo(json, "relatorio_" + investidor.getIdentificador() + ".json");
        System.out.println("Salvo em arquivo json.");
    }

    // Sobrecarga para usar investidor na validação
    private void comprarAtivo(Carteira carteira, Investidor investidor) {
        System.out.print("Ticker: ");
        String ticker = scanner.nextLine();
        Ativo ativo = ativoRepository.buscarPorTicker(ticker);
        if (ativo == null) {
            System.out.println("❌ Ativo não encontrado.");
            return;
        }

        double qtd = lerDouble("Qtd: ");
        double prc = lerDouble("Preço: ");
        System.out.print("Instituição: ");
        String inst = scanner.nextLine();

        try {
            transacaoService.comprar(investidor, ativo, qtd, prc, inst);
            System.out.println("✅ Compra realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + (e.getMessage() != null ? e.getMessage() : "Falha ao processar compra."));
        }
    }

    private void venderAtivo(Carteira carteira, Investidor investidor) {
        System.out.print("Ticker: ");
        String ticker = scanner.nextLine();
        Ativo ativo = ativoRepository.buscarPorTicker(ticker);
        if (ativo == null) {
            System.out.println("❌ Ativo não encontrado.");
            return;
        }

        double qtd = lerDouble("Qtd: ");
        double prc = lerDouble("Preço: ");
        System.out.print("Instituição: ");
        String inst = scanner.nextLine();

        try {
            transacaoService.vender(investidor, ativo, qtd, prc, inst);
            System.out.println("✅ Venda realizada com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro: " + (e.getMessage() != null ? e.getMessage() : "Falha ao processar venda."));
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

}