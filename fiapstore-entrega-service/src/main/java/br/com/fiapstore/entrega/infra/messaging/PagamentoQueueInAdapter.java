package br.com.fiapstore.entrega.infra.messaging;

import br.com.fiapstore.entrega.application.dto.EntregaDto;
import br.com.fiapstore.entrega.domain.exception.EntregaNaoEncontradaException;
import br.com.fiapstore.entrega.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.entrega.domain.messaging.IPagamentoQueueInPort;
import br.com.fiapstore.entrega.domain.usecase.IConfirmarAgendamentoEntregaUseCase;
import br.com.fiapstore.entrega.domain.usecase.IRegistrarAgendamentoEntregaUseCase;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PagamentoQueueInAdapter implements IPagamentoQueueInPort {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IRegistrarAgendamentoEntregaUseCase registrarAgendamentoEntregaUseCase;

    @Autowired
    private IConfirmarAgendamentoEntregaUseCase confirmarAgendamentoEntregaUseCase;

    @Autowired
    private Gson gson;

    @Override
    @RabbitListener(queues = {"${queue2.name}"})
    public void receivePagamentoPendente(String message) {
        logger.info("Mensagem de pagamento pendente recebida: {}", message);
        var entregaDtoInput = fromMessage(message);
        var entregaDtoOutput = this.registrarAgendamentoEntregaUseCase.executar(entregaDtoInput);
        logger.info("Entrega agendada: {}", entregaDtoOutput);
    }

    @Override
    @RabbitListener(queues = {"${queue3.name}"})
    public void receivePagamentoConfirmado(String message) throws EntregaNaoEncontradaException, OperacaoInvalidaException {
        logger.info("Mensagem de pagamento confirmado recebida: {}", message);
        var entregaDtoInput = fromMessage(message);
        var entregaDtoOutput = this.confirmarAgendamentoEntregaUseCase.executar(null, entregaDtoInput.getCodigoPedido());
        logger.info("Entrega agendada: {}", entregaDtoOutput);
    }

    private EntregaDto fromMessage(String message) {
        Map<String, Object> map = gson.fromJson(message, HashMap.class);
        return new EntregaDto(
                null,
                (String) map.get("codigoPedido"),
                (String) map.get("cpf"),
                null,
                null,
                null
        );
    }
}
