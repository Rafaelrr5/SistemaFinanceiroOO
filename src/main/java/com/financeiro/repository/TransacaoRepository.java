package com.financeiro.repository;

import com.financeiro.model.Transacao;
import java.util.ArrayList;
import java.util.List;

public class TransacaoRepository {
    private List<Transacao> transacoes = new ArrayList<>();

    public void salvar(Transacao transacao) {
        transacoes.add(transacao);
    }

    public List<Transacao> listarTodos() {
        return new ArrayList<>(transacoes);
    }
}
