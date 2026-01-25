package com.financeiro.service;

import com.financeiro.model.Investidor;
import com.financeiro.repository.InvestidorRepository;
import java.util.List;

public class InvestidorService {
    private InvestidorRepository repository;

    public InvestidorService(InvestidorRepository repository) {
        this.repository = repository;
    }

    public void cadastrar(Investidor investidor) throws Exception {
        if (investidor.getNome() == null || investidor.getNome().trim().isEmpty()) {
            throw new Exception("Nome é obrigatório.");
        }
        if (investidor.getIdentificador() == null || investidor.getIdentificador().trim().isEmpty()) {
            throw new Exception("Documento (CPF/CNPJ) é obrigatório.");
        }
        if (investidor.getPatrimonio() < 0) {
            throw new Exception("Patrimônio não pode ser negativo.");
        }
        repository.salvar(investidor);
    }

    public List<Investidor> listarTodos() {
        return repository.listarTodos();
    }

    public Investidor buscarPorId(String id) {
        return repository.buscarPorId(id);
    }

    public void excluir(String id) {
        Investidor inv = repository.buscarPorId(id);
        if (inv != null) {
            // A carteira está vinculada ao objeto Investidor, então ao remover o investidor,
            // a carteira "some" junto (GC), a menos que haja referências externas.
            repository.excluir(inv);
        }
    }
    
    public void excluirEmLote(List<String> ids) {
        for (String id : ids) {
            excluir(id.trim());
        }
    }
}
