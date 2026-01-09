package com.financeiro.service;

import com.financeiro.model.Ativo;
import com.financeiro.model.Carteira;
import com.financeiro.repository.CarteiraRepository;
import java.util.List;

public class CarteiraService {
    private CarteiraRepository repository;

    public CarteiraService(CarteiraRepository repository) {
        this.repository = repository;
    }

    public void criarCarteira(Carteira carteira) {
        repository.salvar(carteira);
    }

    public List<Carteira> listarCarteiras() {
        return repository.listarTodos();
    }

    public Carteira buscarCarteira(String nome) throws Exception {
        return repository.buscarPorNome(nome)
                .orElseThrow(() -> new Exception("Carteira não encontrada: " + nome));
    }

    public void adicionarAtivo(Carteira carteira, Ativo ativo, int quantidade) {
        carteira.adicionarAtivo(ativo, quantidade);
        // Em um cenário real com banco de dados, chamaria repository.salvar(carteira) para atualizar
    }

    public void removerAtivo(Carteira carteira, Ativo ativo, int quantidade) {
        carteira.removerAtivo(ativo, quantidade);
        // Em um cenário real com banco de dados, chamaria repository.salvar(carteira) para atualizar
    }
    
    public void adicionarSaldo(Carteira carteira, double valor) {
        if (valor > 0) {
            carteira.setSaldo(carteira.getSaldo() + valor);
        }
    }
}
