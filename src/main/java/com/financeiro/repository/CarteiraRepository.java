package com.financeiro.repository;

import com.financeiro.model.Carteira;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarteiraRepository {
    private List<Carteira> carteiras = new ArrayList<>();

    public void salvar(Carteira carteira) {
        // Simulating save or update
        if (!carteiras.contains(carteira)) {
            carteiras.add(carteira);
        }
    }

    public List<Carteira> listarTodos() {
        return new ArrayList<>(carteiras);
    }

    public Optional<Carteira> buscarPorNome(String nome) {
        return carteiras.stream()
                .filter(c -> c.getNome().equalsIgnoreCase(nome))
                .findFirst();
    }
}
