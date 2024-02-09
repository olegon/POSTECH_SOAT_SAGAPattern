package br.com.fiapstore.cobranca.domain.messaging;

public interface IPedidoQueueInPort {
    void receive(String message);
}
