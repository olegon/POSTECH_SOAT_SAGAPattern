package br.com.fiapstore.pedido.infra.messaging;

import br.com.fiapstore.pedido.domain.messaging.IPedidoQueueOutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PedidoQueueOutAdapter implements IPedidoQueueOutPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue1.name}")
    private String pedidosPendentesQueueName;

    @Override
    public void publish(String message) {
        rabbitTemplate.convertAndSend(pedidosPendentesQueueName, message);
        logger.info("Publicação da mensagem '{}' feita com sucesso para a fila '{}'.", message, pedidosPendentesQueueName);
    }
}
