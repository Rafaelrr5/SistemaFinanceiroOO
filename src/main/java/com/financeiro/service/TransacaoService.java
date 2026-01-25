package com.financeiro.service;

import com.financeiro.model.Ativo;
import com.financeiro.model.Carteira;
import com.financeiro.model.Investidor;
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

    public void comprar(Investidor investidor, Ativo ativo, double quantidade, double preco, String instituicao) throws Exception {
        validacaoService.validarQuantidadePositiva(quantidade);
        validacaoService.validarPermissaoInvestimento(investidor, ativo);
        
        Carteira carteira = investidor.getCarteira();
        double valorTotal = quantidade * preco;
        validacaoService.validarSaldoSuficiente(carteira, valorTotal);

        carteira.setSaldo(carteira.getSaldo() - valorTotal);
        carteira.adicionarAtivo(ativo, quantidade, preco);

        String id = java.util.UUID.randomUUID().toString();
        Transacao transacao = new Transacao(id, TipoTransacao.COMPRA, LocalDateTime.now(), ativo, quantidade, preco, instituicao);
        repository.salvar(transacao);
    }

    public void vender(Investidor investidor, Ativo ativo, double quantidade, double preco, String instituicao) throws Exception {
        validacaoService.validarQuantidadePositiva(quantidade);
        Carteira carteira = investidor.getCarteira();
        validacaoService.validarQuantidadeAtivo(carteira, ativo, quantidade);

        // Venda também pode ter restrições? PDF diz "Ativos qualificados só podem ser movimentados..."
        // Movimentação inclui venda.
        validacaoService.validarPermissaoInvestimento(investidor, ativo);

        double valorTotal = quantidade * preco;
        carteira.setSaldo(carteira.getSaldo() + valorTotal);
        carteira.removerAtivo(ativo, quantidade);

        String id = java.util.UUID.randomUUID().toString();
        Transacao transacao = new Transacao(id, TipoTransacao.VENDA, LocalDateTime.now(), ativo, quantidade, preco, instituicao);
        repository.salvar(transacao);
    }

    public List<Transacao> listarTransacoes() {
        return repository.listarTodos();
    }
}
