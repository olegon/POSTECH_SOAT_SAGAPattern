package br.com.fiapstore.entrega.application.usecase;

import br.com.fiapstore.entrega.application.dto.EntregaDto;
import br.com.fiapstore.entrega.domain.entity.Entrega;
import br.com.fiapstore.entrega.domain.exception.EntregaNaoEncontradaException;
import br.com.fiapstore.entrega.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.entrega.domain.messaging.IEntregaQueueOutPort;
import br.com.fiapstore.entrega.domain.repository.IEntregaDatabasePort;
import br.com.fiapstore.entrega.domain.usecase.IConfirmarAgendamentoEntregaUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfirmarAgendamentoEntrega implements IConfirmarAgendamentoEntregaUseCase {
    @Autowired
    private IEntregaDatabasePort entregaDatabaseAdapter;

    @Autowired
    private IEntregaQueueOutPort entregaQueueOutPort;


    @Override
    public EntregaDto executar(String codigoEntrega, String condigoPedido) throws OperacaoInvalidaException, EntregaNaoEncontradaException {
        Entrega entrega = null;

        if (codigoEntrega != null)
            entrega = this.entregaDatabaseAdapter.findByCodigo(codigoEntrega);
        else
            entrega = this.entregaDatabaseAdapter.findByCodigoPedido(condigoPedido);

        if (entrega == null) throw new EntregaNaoEncontradaException("Entrega não encontrada");

        entrega.confirmarEntrega();

        entrega = entregaDatabaseAdapter.save(entrega);

        entregaQueueOutPort.publishEntregaConfirmada(entrega);

        return EntregaDto.toEntregaDto(entrega);

    }
}
