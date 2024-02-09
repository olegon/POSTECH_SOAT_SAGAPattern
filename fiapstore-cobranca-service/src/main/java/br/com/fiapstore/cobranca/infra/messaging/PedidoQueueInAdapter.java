package br.com.fiapstore.cobranca.infra.messaging;

import br.com.fiapstore.cobranca.application.dto.PagamentoDto;
import br.com.fiapstore.cobranca.domain.messaging.IPedidoQueueInPort;
import br.com.fiapstore.cobranca.domain.usecase.IRegistrarPagamentoUseCase;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PedidoQueueInAdapter implements IPedidoQueueInPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Gson gson;

    @Autowired
    private IRegistrarPagamentoUseCase registrarPagamentoUseCase;

    @RabbitListener(queues = {"${queue1.name}"})
    @Override
    public void receive(String message) {
        logger.info("Mensagem de pedido recebida: {}", message);
        Map<String, Object> mensagem = gson.fromJson(message, HashMap.class);
        PagamentoDto pagamentoDto = fromMap(mensagem);

        registrarPagamentoUseCase.executar(pagamentoDto);
        logger.info("Pagamento registrado: {}", pagamentoDto);
    }

    private PagamentoDto fromMap(Map<String, Object> mensagem) {
        return new PagamentoDto(
                null,
                (String) mensagem.get("codigoPedido"),
                (Double) mensagem.get("precoTotal"),
                (Double) mensagem.get("percentualDesconto"),
                (String) mensagem.get("cpf"),
                null,
                null
        );
    }
}
