package br.com.fiapstore.pedido.infra.messaging;

import br.com.fiapstore.pedido.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.pedido.domain.exception.PedidoNaoEncontradoException;
import br.com.fiapstore.pedido.domain.exception.PercentualDescontoAcimaDoLimiteException;
import br.com.fiapstore.pedido.domain.messaging.IEntregaQueueInPort;
import br.com.fiapstore.pedido.domain.usecase.ConfirmarPedidoUseCase;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EntregaQueueInAdapter implements IEntregaQueueInPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ConfirmarPedidoUseCase confirmarPedidoUseCase;

    @Autowired
    private Gson gson;

    @Override
    @RabbitListener(queues = {"${queue4.name}"})
    public void receiveEntregaConfirmada(String message) throws PedidoNaoEncontradoException, PercentualDescontoAcimaDoLimiteException, OperacaoInvalidaException {
        logger.info("Mensagem de entrega confirmada recebida: {}", message);
        var codigoPedido = getCodigoPedidoFromMessage(message);
        this.confirmarPedidoUseCase.executar(codigoPedido);
        logger.info("Pedido confirmado: {}", codigoPedido);
    }

    private String getCodigoPedidoFromMessage(String message) {
        Map<String, Object> map = gson.fromJson(message, HashMap.class);
        return (String) map.get("codigoPedido");
    }
}
