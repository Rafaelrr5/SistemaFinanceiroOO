package com.financeiro.repository;

import com.financeiro.model.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AtivoRepository implements Repositorio<Ativo> {
    private List<Ativo> ativos = new ArrayList<>();

    public AtivoRepository() {
        carregarAcoes();
        carregarFIIs();
        carregarCriptoativos();
        carregarStocks();
        carregarTesouros();
    }

    @Override
    public void salvar(Ativo ativo) {
        if (ativo == null) return;
        Ativo existente = buscarPorId(ativo.getTicker());
        if (existente != null) {
            ativos.remove(existente);
        }
        ativos.add(ativo);
    }

    @Override
    public List<Ativo> listarTodos() {
        return new ArrayList<>(ativos);
    }

    @Override
    public Ativo buscarPorId(String ticker) {
        return buscarPorTicker(ticker);
    }

    @Override
    public void excluir(Ativo ativo) {
        ativos.remove(ativo);
    }

    // Métodos específicos mantidos
    private void carregarAcoes() {
        try (BufferedReader br = new BufferedReader(new FileReader("acao.csv"))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                String[] dados = linha.split(";");
                if (dados.length >= 4) {
                    String ticker = dados[0].trim();
                    String nome = dados[1].trim();
                    double preco = parseDouble(dados[2].replace(",", "."));
                    boolean qualificado = "1".equals(dados[3].trim());
                    ativos.add(new Acao(ticker, nome, preco, qualificado));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar ações: " + e.getMessage());
        }
    }

    private void carregarFIIs() {
        try (BufferedReader br = new BufferedReader(new FileReader("fii.csv"))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                String[] dados = linha.split(";");
                if (dados.length >= 6) {
                    String ticker = dados[0].trim();
                    String nome = dados[1].trim();
                    String setor = dados[2].trim();
                    double preco = parseDouble(dados[3].replace(",", "."));
                    double ultimoDividendo = parseDouble(dados[4].replace(",", "."));
                    double taxaAdmin = parseDouble(dados[5].replace(",", "."));
                    ativos.add(new FII(ticker, nome, preco, setor, ultimoDividendo, taxaAdmin));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar FIIs: " + e.getMessage());
        }
    }

    private void carregarCriptoativos() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/criptoativo.csv"))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                String[] dados = linha.split(";");
                if (dados.length >= 5) {
                    String ticker = dados[0].trim();
                    String nome = dados[1].trim();
                    double precoUSD = parseDouble(dados[2].replace(",", "."));
                    String algoritmo = dados[3].trim();
                    double qtdMaxima = parseDouble(dados[4].replace(",", "."));
                    ativos.add(new Criptoativo(ticker, nome, precoUSD, algoritmo, qtdMaxima, 5.39));
                }
            }
        } catch (IOException e) {
            try (BufferedReader br = new BufferedReader(new FileReader("criptoativo.csv"))) {
                 String linha;
                 boolean primeira = true;
                 while ((linha = br.readLine()) != null) {
                     if (primeira) {
                         primeira = false;
                         continue;
                     }
                     String[] dados = linha.split(";");
                     if (dados.length >= 5) {
                         String ticker = dados[0].trim();
                         String nome = dados[1].trim();
                         double precoUSD = parseDouble(dados[2].replace(",", "."));
                         String algoritmo = dados[3].trim();
                         double qtdMaxima = parseDouble(dados[4].replace(",", "."));
                         ativos.add(new Criptoativo(ticker, nome, precoUSD, algoritmo, qtdMaxima, 5.39));
                     }
                 }
            } catch (IOException ex) {
                System.out.println("Erro ao carregar criptoativos: " + e.getMessage());
            }
        }
    }

    private void carregarStocks() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/stock.csv"))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                String[] dados = linha.split(";");
                if (dados.length >= 5) {
                    String ticker = dados[0].trim();
                    String nome = dados[1].trim();
                    double precoUSD = parseDouble(dados[2].replace(",", "."));
                    String bolsa = dados[3].trim();
                    String setor = dados[4].trim();
                    ativos.add(new Stock(ticker, nome, precoUSD, bolsa, setor, 5.39));
                }
            }
        } catch (IOException e) {
             try (BufferedReader br = new BufferedReader(new FileReader("stock.csv"))) {
                String linha;
                boolean primeira = true;
                while ((linha = br.readLine()) != null) {
                    if (primeira) {
                        primeira = false;
                        continue;
                    }
                    String[] dados = linha.split(";");
                    if (dados.length >= 5) {
                        String ticker = dados[0].trim();
                        String nome = dados[1].trim();
                        double precoUSD = parseDouble(dados[2].replace(",", "."));
                        String bolsa = dados[3].trim();
                        String setor = dados[4].trim();
                        ativos.add(new Stock(ticker, nome, precoUSD, bolsa, setor, 5.39));
                    }
                }
            } catch (IOException ex) {
                System.out.println("Erro ao carregar stocks: " + e.getMessage());
            }
        }
    }

    private void carregarTesouros() {
        try (BufferedReader br = new BufferedReader(new FileReader("tesouro.csv"))) {
            String linha;
            boolean primeira = true;
            while ((linha = br.readLine()) != null) {
                if (primeira) {
                    primeira = false;
                    continue;
                }
                String[] dados = linha.split(";");
                if (dados.length >= 5) {
                    String ticker = dados[0].trim();
                    String nome = dados[1].trim();
                    double preco = parseDouble(dados[2].replace(",", "."));
                    String tipoRendimento = dados[3].trim();
                    String vencimento = dados[4].trim();
                    ativos.add(new Tesouro(ticker, nome, preco, tipoRendimento, vencimento));
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar tesouros: " + e.getMessage());
        }
    }

    private double parseDouble(String valor) {
        try {
            return Double.parseDouble(valor);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Ativo buscarPorTicker(String ticker) {
        return ativos.stream()
                .filter(a -> a.getTicker().equalsIgnoreCase(ticker))
                .findFirst()
                .orElse(null);
    }
    
    // Alias para manter compatibilidade ou pode ser removido refatorando quem chama
    public void adicionarAtivo(Ativo ativo) {
        salvar(ativo);
    }

    public boolean removerAtivo(String ticker) {
        Ativo ativo = buscarPorId(ticker);
        if (ativo != null) {
            excluir(ativo);
            return true;
        }
        return false;
    }
}
