package com.financeiro.repository;

import java.util.List;

public interface Repositorio<T> {
    void salvar(T entidade);
    List<T> listarTodos();
    T buscarPorId(String id);
    void excluir(T entidade);
}
