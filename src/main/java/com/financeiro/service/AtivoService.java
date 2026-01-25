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
        // Atualiza mantendo o mesmo objeto ou substitui no repo
        // Como o repo usa List e remove/add, substituir funciona.
        cadastrar(ativoNovo);
        
        // Se quiséssemos propagar alterações de nome/preço para carteiras (embora carteira referencie objeto),
        // se substituirmos o objeto no repo, as carteiras ainda apontam pro objeto ANTIGO se não atualizarmos.
        // O ideal é atualizar os campos do objeto existente ou atualizar referências.
        // Dado o escopo, atualizar campos seria melhor, mas Ativo tem subclasses.
        // Vamos varrer investidores e atualizar referências.
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
            // Precisamos procurar pela chave (Ativo) com aquele ticker
            Ativo chaveEncontrada = null;
            for (Ativo a : carteira.getAtivos().keySet()) {
                if (a.getTicker().equals(tickerAntigo)) {
                    chaveEncontrada = a;
                    break;
                }
            }
            
            if (chaveEncontrada != null) {
                var item = carteira.getAtivos().remove(chaveEncontrada);
                item.setAtivo(novoAtivo); // Atualiza item
                carteira.getAtivos().put(novoAtivo, item); // Recoloca com nova chave
            }
        }
    }
}
