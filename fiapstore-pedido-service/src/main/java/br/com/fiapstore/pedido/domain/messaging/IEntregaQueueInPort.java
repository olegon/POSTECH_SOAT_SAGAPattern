package br.com.fiapstore.pedido.domain.messaging;

import br.com.fiapstore.pedido.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.pedido.domain.exception.PedidoNaoEncontradoException;
import br.com.fiapstore.pedido.domain.exception.PercentualDescontoAcimaDoLimiteException;

public interface IEntregaQueueInPort {
    void receiveEntregaConfirmada(String message) throws PedidoNaoEncontradoException, PercentualDescontoAcimaDoLimiteException, OperacaoInvalidaException;
}
