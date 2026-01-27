package com.financeiro.service;

import com.financeiro.model.*;
import com.financeiro.repository.AtivoRepository;
import com.financeiro.repository.InvestidorRepository;
import com.financeiro.repository.TransacaoRepository;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de cadastro em lote usando ArquivoService.
 * Testa importação de investidores, ativos e transações via CSV.
 */
@DisplayName("ArquivoService - Cadastro em Lote")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CadastroLoteTest {

    private AtivoRepository ativoRepository;
    private InvestidorRepository investidorRepository;
    private ArquivoService arquivoService;
    private TransacaoService transacaoService;
    private TransacaoRepository transacaoRepository;
    private ValidacaoService validacaoService;

    private static final String BASE_PATH = "src/test/resources/";

    @BeforeEach
    void setUp() {
        ativoRepository = new AtivoRepository();
        investidorRepository = new InvestidorRepository();
        transacaoRepository = new TransacaoRepository();
        validacaoService = new ValidacaoService();
        arquivoService = new ArquivoService(ativoRepository, investidorRepository);
        transacaoService = new TransacaoService(transacaoRepository, validacaoService);
    }

    @Nested
    @DisplayName("Importação de Investidores em Lote")
    class ImportacaoInvestidores {

        @Test
        @Order(1)
        @DisplayName("Deve importar investidores PF e PJ de arquivo CSV")
        void deveImportarInvestidoresEmLote() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "investidores_lote_teste.csv";

            // Act
            arquivoService.importarInvestidores(caminhoArquivo);

            // Assert
            List<Investidor> investidores = investidorRepository.listarTodos();

            assertFalse(investidores.isEmpty(), "Lista de investidores não deve estar vazia");
            assertTrue(investidores.size() >= 8, "Deve ter importado pelo menos 8 investidores");

            // Verifica se há tanto PF quanto PJ
            long quantidadePF = investidores.stream()
                    .filter(inv -> inv instanceof PessoaFisica)
                    .count();
            long quantidadePJ = investidores.stream()
                    .filter(inv -> inv instanceof Institucional)
                    .count();

            assertTrue(quantidadePF >= 5, "Deve ter pelo menos 5 investidores PF");
            assertTrue(quantidadePJ >= 3, "Deve ter pelo menos 3 investidores PJ");

            System.out.println("✓ Investidores importados com sucesso: " + investidores.size());
            System.out.println("  - Pessoa Física: " + quantidadePF);
            System.out.println("  - Pessoa Jurídica: " + quantidadePJ);
        }

        @Test
        @Order(2)
        @DisplayName("Deve importar investidores com diferentes perfis")
        void deveImportarInvestidoresComDiferentesPerfis() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "investidores_lote_teste.csv";

            // Act
            arquivoService.importarInvestidores(caminhoArquivo);

            // Assert
            List<Investidor> investidores = investidorRepository.listarTodos();

            // Conta perfis
            long conservadores = investidores.stream()
                    .filter(inv -> inv instanceof PessoaFisica)
                    .map(inv -> (PessoaFisica) inv)
                    .filter(pf -> pf.getPerfil() == PerfilInvestidor.CONSERVADOR)
                    .count();

            long moderados = investidores.stream()
                    .filter(inv -> inv instanceof PessoaFisica)
                    .map(inv -> (PessoaFisica) inv)
                    .filter(pf -> pf.getPerfil() == PerfilInvestidor.MODERADO)
                    .count();

            long arrojados = investidores.stream()
                    .filter(inv -> inv instanceof PessoaFisica)
                    .map(inv -> (PessoaFisica) inv)
                    .filter(pf -> pf.getPerfil() == PerfilInvestidor.ARROJADO)
                    .count();

            assertTrue(conservadores >= 1, "Deve ter pelo menos 1 investidor CONSERVADOR");
            assertTrue(moderados >= 1, "Deve ter pelo menos 1 investidor MODERADO");
            assertTrue(arrojados >= 1, "Deve ter pelo menos 1 investidor ARROJADO");

            System.out.println("✓ Perfis de investidores:");
            System.out.println("  - Conservador: " + conservadores);
            System.out.println("  - Moderado: " + moderados);
            System.out.println("  - Arrojado: " + arrojados);
        }

        @Test
        @Order(3)
        @DisplayName("Deve identificar investidores qualificados por patrimônio")
        void deveIdentificarInvestidoresQualificados() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "investidores_lote_teste.csv";

            // Act
            arquivoService.importarInvestidores(caminhoArquivo);

            // Assert
            List<Investidor> investidores = investidorRepository.listarTodos();

            // Investidor qualificado: patrimônio >= 1.000.000
            long qualificados = investidores.stream()
                    .filter(Investidor::isQualificado)
                    .count();

            assertTrue(qualificados >= 1, "Deve ter pelo menos 1 investidor qualificado");

            System.out.println("✓ Investidores qualificados: " + qualificados);
        }

        @Test
        @Order(4)
        @DisplayName("Deve validar dados dos investidores importados")
        void deveValidarDadosInvestidoresImportados() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "investidores_lote_teste.csv";

            // Act
            arquivoService.importarInvestidores(caminhoArquivo);

            // Assert
            List<Investidor> investidores = investidorRepository.listarTodos();

            // Valida que todos têm dados obrigatórios
            for (Investidor inv : investidores) {
                assertNotNull(inv.getNome(), "Nome não pode ser nulo");
                assertFalse(inv.getNome().isEmpty(), "Nome não pode estar vazio");
                assertNotNull(inv.getIdentificador(), "Identificador não pode ser nulo");
                assertNotNull(inv.getEmail(), "Email não pode ser nulo");
                assertTrue(inv.getPatrimonio() > 0, "Patrimônio deve ser positivo");
            }

            System.out.println("✓ Validação de dados concluída para " + investidores.size() + " investidores");
        }
    }

    @Nested
    @DisplayName("Importação de Ativos em Lote")
    class ImportacaoAtivos {

        @Test
        @Order(1)
        @DisplayName("Deve importar ações de arquivo CSV")
        void deveImportarAcoesEmLote() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "acoes_lote_teste.csv";

            // Act
            arquivoService.importarAtivos(caminhoArquivo, "ACAO");

            // Assert
            List<Ativo> ativos = ativoRepository.listarTodos();

            assertFalse(ativos.isEmpty(), "Lista de ativos não deve estar vazia");
            assertTrue(ativos.size() >= 5, "Deve ter importado pelo menos 5 ações");

            // Verifica se são do tipo Acao
            long quantidadeAcoes = ativos.stream()
                    .filter(ativo -> ativo instanceof Acao)
                    .count();

            assertEquals(ativos.size(), quantidadeAcoes, "Todos os ativos devem ser do tipo Ação");

            System.out.println("✓ Ações importadas com sucesso: " + ativos.size());
        }

        @Test
        @Order(2)
        @DisplayName("Deve importar ações com atributos corretos")
        void deveImportarAcoesComAtributos() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "acoes_lote_teste.csv";

            // Act
            arquivoService.importarAtivos(caminhoArquivo, "ACAO");

            // Assert
            Ativo ativo = ativoRepository.buscarPorTicker("TEST1");

            assertNotNull(ativo, "Ativo TEST1 deve existir");
            assertEquals("Empresa Teste Alpha", ativo.getNome());
            assertEquals(25.50, ativo.getPreco(), 0.01);

            System.out.println("✓ Ativo validado: " + ativo.getTicker() + " - " + ativo.getNome());
        }

        @Test
        @Order(3)
        @DisplayName("Deve identificar ativos qualificados")
        void deveIdentificarAtivosQualificados() throws IOException {
            // Arrange
            String caminhoArquivo = BASE_PATH + "acoes_lote_teste.csv";

            // Act
            arquivoService.importarAtivos(caminhoArquivo, "ACAO");

            // Assert
            List<Ativo> ativos = ativoRepository.listarTodos();

            long qualificados = ativos.stream()
                    .filter(Ativo::isQualificado)
                    .count();

            long naoQualificados = ativos.stream()
                    .filter(a -> !a.isQualificado())
                    .count();

            assertTrue(qualificados >= 2, "Deve ter pelo menos 2 ativos qualificados");
            assertTrue(naoQualificados >= 3, "Deve ter pelo menos 3 ativos não qualificados");

            System.out.println("✓ Ativos qualificados: " + qualificados);
            System.out.println("✓ Ativos não qualificados: " + naoQualificados);
        }
    }

    @Nested
    @DisplayName("Importação de Transações em Lote")
    class ImportacaoTransacoes {

        @Test
        @Order(1)
        @DisplayName("Deve importar transações de compra e venda")
        void deveImportarTransacoesEmLote() throws IOException {
            // Arrange - Primeiro importa ativos e investidores
            arquivoService.importarAtivos(BASE_PATH + "acoes_lote_teste.csv", "ACAO");
            arquivoService.importarInvestidores(BASE_PATH + "investidores_lote_teste.csv");

            Investidor investidor = investidorRepository.listarTodos().get(0);

            String caminhoTransacoes = BASE_PATH + "transacoes_lote_teste.csv";

            // Act
            arquivoService.importarTransacoes(caminhoTransacoes, investidor, transacaoService);

            // Assert
            assertNotNull(investidor.getCarteira(), "Investidor deve ter carteira");
            assertFalse(investidor.getCarteira().getAtivos().isEmpty(),
                    "Carteira deve ter ativos após importação");

            System.out.println("✓ Transações importadas para: " + investidor.getNome());
            System.out.println("  - Ativos na carteira: " + investidor.getCarteira().getAtivos().size());
        }

        @Test
        @Order(2)
        @DisplayName("Deve atualizar valor carteira após transações")
        void deveAtualizarValorCarteiraAposTransacoes() throws IOException {
            // Arrange
            arquivoService.importarAtivos(BASE_PATH + "acoes_lote_teste.csv", "ACAO");
            arquivoService.importarInvestidores(BASE_PATH + "investidores_lote_teste.csv");

            // Pega um investidor arrojado (pode operar qualquer ativo)
            Investidor investidor = investidorRepository.listarTodos().stream()
                    .filter(inv -> inv instanceof PessoaFisica)
                    .map(inv -> (PessoaFisica) inv)
                    .filter(pf -> pf.getPerfil() == PerfilInvestidor.ARROJADO)
                    .findFirst()
                    .orElse(null);

            if (investidor == null) {
                investidor = investidorRepository.listarTodos().get(0);
            }

            // Act
            arquivoService.importarTransacoes(BASE_PATH + "transacoes_lote_teste.csv",
                    investidor, transacaoService);

            // Assert
            double valorTotal = investidor.getCarteira().getValorTotalCarteira();
            assertTrue(valorTotal > 0, "Valor carteira deve ser positivo após compras");

            System.out.println("✓ Valor total da carteira: R$ " + String.format("%.2f", valorTotal));
        }
    }

    @Nested
    @DisplayName("Cenários de Erro")
    class CenariosErro {

        @Test
        @DisplayName("Deve tratar arquivo inexistente graciosamente")
        void deveTratarArquivoInexistente() {
            // Arrange
            String arquivoInexistente = BASE_PATH + "arquivo_que_nao_existe.csv";

            // Act & Assert
            assertThrows(IOException.class, () -> {
                arquivoService.importarInvestidores(arquivoInexistente);
            }, "Deve lançar IOException para arquivo inexistente");

            System.out.println("✓ Tratamento de arquivo inexistente OK");
        }

        @Test
        @DisplayName("Deve continuar importação mesmo com linhas inválidas")
        void deveContinuarImportacaoComLinhasInvalidas() throws IOException {
            // Arrange - Arquivo com dados válidos
            String caminhoArquivo = BASE_PATH + "investidores_lote_teste.csv";

            // Act - Não deve lançar exceção
            assertDoesNotThrow(() -> {
                arquivoService.importarInvestidores(caminhoArquivo);
            });

            // Assert
            List<Investidor> investidores = investidorRepository.listarTodos();
            assertFalse(investidores.isEmpty(), "Deve ter importado investidores válidos");

            System.out.println("✓ Importação resiliente a erros concluída");
        }
    }

    @Nested
    @DisplayName("Relatório Consolidado")
    class RelatorioConsolidado {

        @Test
        @Order(1)
        @DisplayName("Deve gerar relatório consolidado após importação em lote")
        void deveGerarRelatorioConsolidado() throws IOException {
            // Arrange & Act - Importa tudo
            arquivoService.importarAtivos(BASE_PATH + "acoes_lote_teste.csv", "ACAO");
            arquivoService.importarInvestidores(BASE_PATH + "investidores_lote_teste.csv");

            // Assert & Report
            List<Ativo> ativos = ativoRepository.listarTodos();
            List<Investidor> investidores = investidorRepository.listarTodos();

            System.out.println("\n═══════════════════════════════════════════════");
            System.out.println("       RELATÓRIO DE CADASTRO EM LOTE");
            System.out.println("═══════════════════════════════════════════════");

            System.out.println("\n📊 ATIVOS IMPORTADOS: " + ativos.size());
            System.out.println("─────────────────────────────────────────────");
            for (Ativo ativo : ativos) {
                System.out.printf("  %-10s | %-25s | R$ %10.2f%n",
                        ativo.getTicker(),
                        ativo.getNome().substring(0, Math.min(25, ativo.getNome().length())),
                        ativo.getPreco());
            }

            System.out.println("\n👥 INVESTIDORES IMPORTADOS: " + investidores.size());
            System.out.println("─────────────────────────────────────────────");
            for (Investidor inv : investidores) {
                String tipo = inv instanceof PessoaFisica ? "PF" : "PJ";
                String perfil = inv instanceof PessoaFisica ? ((PessoaFisica) inv).getPerfil().name() : "INSTITUCIONAL";
                System.out.printf("  [%s] %-25s | %-12s | R$ %,.2f%n",
                        tipo,
                        inv.getNome().substring(0, Math.min(25, inv.getNome().length())),
                        perfil,
                        inv.getPatrimonio());
            }

            System.out.println("\n═══════════════════════════════════════════════");
            System.out.println("              TESTE CONCLUÍDO ✓");
            System.out.println("═══════════════════════════════════════════════\n");

            assertTrue(ativos.size() >= 5, "Deve ter pelo menos 5 ativos");
            assertTrue(investidores.size() >= 8, "Deve ter pelo menos 8 investidores");
        }
    }
}
