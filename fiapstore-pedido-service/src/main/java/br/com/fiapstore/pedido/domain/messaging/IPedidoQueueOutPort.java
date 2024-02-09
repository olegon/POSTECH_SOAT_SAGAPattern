package br.com.fiapstore.pedido.domain.messaging;

public interface IPedidoQueueOutPort {
    void publish(String message);
}
