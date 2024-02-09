package br.com.fiapstore.cobranca.infra.messaging;

import br.com.fiapstore.cobranca.domain.messaging.IPagamentoQueueOutPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PagamentoQueueOutAdapter implements IPagamentoQueueOutPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${queue2.name}")
    private String pagamentoPendenteQueueName;

    @Value("${queue3.name}")
    private String pagamentoConfirmadoQueueName;

    @Override
    public void publishPagamentoPendente(String pagamentoJson) {
        rabbitTemplate.convertAndSend(pagamentoPendenteQueueName, pagamentoJson);
        logger.info("Publicação da mensagem '{}' feita com sucesso para a fila '{}'.", pagamentoJson, pagamentoPendenteQueueName);
    }

    @Override
    public void publishPagamentoConfirmado(String pagamentoJson) {
        rabbitTemplate.convertAndSend(pagamentoConfirmadoQueueName, pagamentoJson);
        logger.info("Publicação da mensagem '{}' feita com sucesso para a fila '{}'.", pagamentoJson, pagamentoConfirmadoQueueName);
    }
}
