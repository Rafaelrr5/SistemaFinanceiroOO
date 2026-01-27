package com.financeiro.service;

import com.financeiro.model.Ativo;
import com.financeiro.model.Carteira;
import com.financeiro.model.Investidor;
import com.financeiro.repository.AtivoRepository;
import com.financeiro.repository.InvestidorRepository;
import java.util.List;

public class AtivoService {
    private AtivoRepository ativoRepository;
    private InvestidorRepository investidorRepository;

    public AtivoService(AtivoRepository ativoRepository, InvestidorRepository investidorRepository) {
        this.ativoRepository = ativoRepository;
        this.investidorRepository = investidorRepository;
    }

    public void cadastrar(Ativo ativo) throws Exception {
        if (ativo.getTicker() == null || ativo.getTicker().trim().isEmpty()) {
            throw new Exception("Ticker não pode ser nulo ou vazio.");
        }
        if (ativo.getPreco() <= 0) {
            throw new Exception("Preço deve ser maior que zero.");
        }
        ativoRepository.salvar(ativo);
    }

    public void editar(Ativo ativoNovo) throws Exception {
        Ativo existente = ativoRepository.buscarPorId(ativoNovo.getTicker());
        if (existente == null) {
            throw new Exception("Ativo não encontrado para edição.");
        }
        cadastrar(ativoNovo);

        atualizarReferenciaEmCarteiras(existente.getTicker(), ativoNovo);
    }

    public void excluir(String ticker) {
        Ativo ativo = ativoRepository.buscarPorId(ticker);
        if (ativo != null) {
            ativoRepository.excluir(ativo);
            removerDeCarteiras(ativo);
        }
    }

    private void removerDeCarteiras(Ativo ativo) {
        List<Investidor> investidores = investidorRepository.listarTodos();
        for (Investidor inv : investidores) {
            Carteira carteira = inv.getCarteira();
            if (carteira.getAtivos().containsKey(ativo)) {
                carteira.getAtivos().remove(ativo);
            }
        }
    }

    private void atualizarReferenciaEmCarteiras(String tickerAntigo, Ativo novoAtivo) {
        List<Investidor> investidores = investidorRepository.listarTodos();
        for (Investidor inv : investidores) {
            Carteira carteira = inv.getCarteira();
            Ativo chaveEncontrada = null;
            for (Ativo a : carteira.getAtivos().keySet()) {
                if (a.getTicker().equals(tickerAntigo)) {
                    chaveEncontrada = a;
                    break;
                }
            }

            if (chaveEncontrada != null) {
                var item = carteira.getAtivos().remove(chaveEncontrada);
                item.setAtivo(novoAtivo);
                carteira.getAtivos().put(novoAtivo, item);
            }
        }
    }
}
