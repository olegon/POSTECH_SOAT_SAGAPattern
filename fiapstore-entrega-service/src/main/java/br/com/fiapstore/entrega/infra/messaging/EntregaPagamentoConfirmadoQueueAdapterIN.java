package br.com.fiapstore.entrega.infra.messaging;

import br.com.fiapstore.entrega.application.dto.EntregaDto;
import br.com.fiapstore.entrega.application.usecase.ConfirmarAgendamentoEntrega;
import br.com.fiapstore.entrega.domain.entity.Entrega;
import br.com.fiapstore.entrega.domain.exception.EntregaNaoEncontradaException;
import br.com.fiapstore.entrega.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.entrega.domain.repository.IEntregaQueueAdapterIN;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EntregaPagamentoConfirmadoQueueAdapterIN implements IEntregaQueueAdapterIN {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Gson gson;

    private final ConfirmarAgendamentoEntrega confirmarAgendamentoEntrega;


    @Autowired
    public EntregaPagamentoConfirmadoQueueAdapterIN(ConfirmarAgendamentoEntrega confirmarAgendamentoEntrega){
        this.confirmarAgendamentoEntrega = confirmarAgendamentoEntrega;
    }

    @Override
    @RabbitListener(queues = {"${queue3.name}"})
    public void receive(@Payload String message) throws EntregaNaoEncontradaException, OperacaoInvalidaException {

        HashMap<String, String> mensagem = gson.fromJson(message, HashMap.class);

        EntregaDto entregaDto = IEntregaQueueAdapterIN.fromMessageToDto(mensagem);

        confirmarAgendamentoEntrega.executar(null,entregaDto.getCodigoPedido());
        logger.info("Confirmação de entrega registrada",entregaDto);
    }

}
