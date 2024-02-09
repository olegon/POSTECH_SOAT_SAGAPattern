package br.com.fiapstore.entrega.domain.messaging;

import br.com.fiapstore.entrega.domain.entity.Entrega;

public interface IEntregaQueueOutPort {
    void publishEntregaConfirmada(Entrega entrega);
}
