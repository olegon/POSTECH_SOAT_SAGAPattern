package br.com.fiapstore.entrega.domain.messaging;

import br.com.fiapstore.entrega.domain.exception.EntregaNaoEncontradaException;
import br.com.fiapstore.entrega.domain.exception.OperacaoInvalidaException;

public interface IPagamentoQueueInPort {
    void receivePagamentoPendente(String message);

    void receivePagamentoConfirmado(String message) throws EntregaNaoEncontradaException, OperacaoInvalidaException;
}
