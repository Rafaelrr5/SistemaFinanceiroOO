package com.financeiro.service;

import com.financeiro.model.Ativo;
import com.financeiro.model.Carteira;
import com.financeiro.model.TipoTransacao;
import com.financeiro.model.Transacao;
import com.financeiro.repository.TransacaoRepository;
import java.time.LocalDateTime;
import java.util.List;

public class TransacaoService {
    private TransacaoRepository repository;
    private ValidacaoService validacaoService;

    public TransacaoService(TransacaoRepository repository, ValidacaoService validacaoService) {
        this.repository = repository;
        this.validacaoService = validacaoService;
    }

    public void comprar(Carteira carteira, Ativo ativo, int quantidade, double preco) throws Exception {
        validacaoService.validarQuantidadePositiva(quantidade);
        double valorTotal = quantidade * preco;
        validacaoService.validarSaldoSuficiente(carteira, valorTotal);

        carteira.setSaldo(carteira.getSaldo() - valorTotal);
        carteira.adicionarAtivo(ativo, quantidade);

        Transacao transacao = new Transacao(TipoTransacao.COMPRA, LocalDateTime.now(), ativo, quantidade, preco);
        repository.salvar(transacao);
    }

    public void vender(Carteira carteira, Ativo ativo, int quantidade, double preco) throws Exception {
        validacaoService.validarQuantidadePositiva(quantidade);
        validacaoService.validarQuantidadeAtivo(carteira, ativo, quantidade);

        double valorTotal = quantidade * preco;
        carteira.setSaldo(carteira.getSaldo() + valorTotal);
        carteira.removerAtivo(ativo, quantidade);

        Transacao transacao = new Transacao(TipoTransacao.VENDA, LocalDateTime.now(), ativo, quantidade, preco);
        repository.salvar(transacao);
    }

    public List<Transacao> listarTransacoes() {
        return repository.listarTodos();
    }
}
