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

    /**
     * Registra uma compra de ativo para o investidor.
     * Valida permissões de perfil antes de executar.
     * A compra adiciona o ativo à carteira e registra a transação.
     */
    public void comprar(Investidor investidor, Ativo ativo, double quantidade, double preco, String instituicao) throws Exception {
        validacaoService.validarQuantidadePositiva(quantidade);
        validacaoService.validarPrecoPositivo(preco);
        validacaoService.validarPermissaoInvestimento(investidor, ativo);
        
        Carteira carteira = investidor.getCarteira();
        
        // Adiciona o ativo à carteira (compra)
        carteira.adicionarAtivo(ativo, quantidade, preco);

        // Registra a transação
        String id = java.util.UUID.randomUUID().toString();
        Transacao transacao = new Transacao(id, TipoTransacao.COMPRA, LocalDateTime.now(), ativo, quantidade, preco, instituicao);
        repository.salvar(transacao);
    }

    /**
     * Registra uma venda de ativo para o investidor.
     * Valida quantidade disponível antes de executar.
     * A venda não pode exceder a quantidade que o investidor possui.
     */
    public void vender(Investidor investidor, Ativo ativo, double quantidade, double preco, String instituicao) throws Exception {
        validacaoService.validarQuantidadePositiva(quantidade);
        validacaoService.validarPrecoPositivo(preco);
        Carteira carteira = investidor.getCarteira();
        validacaoService.validarQuantidadeAtivo(carteira, ativo, quantidade);

        // Validação de permissão também para venda (conforme PDF: "movimentação" inclui compra e venda)
        validacaoService.validarPermissaoInvestimento(investidor, ativo);

        // Remove o ativo da carteira (venda)
        carteira.removerAtivo(ativo, quantidade);

        // Registra a transação
        String id = java.util.UUID.randomUUID().toString();
        Transacao transacao = new Transacao(id, TipoTransacao.VENDA, LocalDateTime.now(), ativo, quantidade, preco, instituicao);
        repository.salvar(transacao);
    }

    public List<Transacao> listarTransacoes() {
        return repository.listarTodos();
    }
}
