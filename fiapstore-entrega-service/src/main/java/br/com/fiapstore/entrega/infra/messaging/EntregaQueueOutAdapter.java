package br.com.fiapstore.entrega.infra.messaging;

import br.com.fiapstore.entrega.domain.entity.Entrega;
import br.com.fiapstore.entrega.domain.messaging.IEntregaQueueOutPort;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class EntregaQueueOutAdapter implements IEntregaQueueOutPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue4.name}")
    private String entregaConfirmadaQueueName;

    @Autowired
    private Gson gson;

    @Override
    public void publishEntregaConfirmada(Entrega entrega) {
        var message = toMessage(entrega);
        rabbitTemplate.convertAndSend(entregaConfirmadaQueueName, message);
        logger.info("Publicação da mensagem '{}' feita com sucesso para a fila '{}'.", message, entregaConfirmadaQueueName);
    }

    private String toMessage(Entrega entrega) {
        var map = new HashMap<String, Object>();

        map.put("codigoPedido", entrega.getCodigoPedido());
        map.put("codigoEntrega", entrega.getCodigo());
        map.put("cpf", entrega.getCpf());

        return gson.toJson(map);
    }
}
