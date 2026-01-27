package com.financeiro.repository;

import com.financeiro.model.Investidor;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InvestidorRepository implements Repositorio<Investidor> {
    private List<Investidor> investidores = new ArrayList<>();

    @Override
    public void salvar(Investidor investidor) {
        Investidor existente = buscarPorId(investidor.getIdentificador());
        if (existente != null) {
            investidores.remove(existente);
        }
        investidores.add(investidor);
    }

    @Override
    public List<Investidor> listarTodos() {
        return new ArrayList<>(investidores);
    }

    @Override
    public Investidor buscarPorId(String identificador) {
        return investidores.stream()
                .filter(i -> i.getIdentificador().equals(identificador))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void excluir(Investidor investidor) {
        investidores.remove(investidor);
    }

    public boolean excluirPorId(String identificador) {
        Investidor inv = buscarPorId(identificador);
        if (inv != null) {
            excluir(inv);
            return true;
        }
        return false;
    }
}
