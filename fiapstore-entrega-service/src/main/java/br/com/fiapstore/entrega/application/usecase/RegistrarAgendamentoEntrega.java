package br.com.fiapstore.entrega.application.usecase;

import br.com.fiapstore.entrega.application.dto.EntregaDto;
import br.com.fiapstore.entrega.domain.entity.Entrega;
import br.com.fiapstore.entrega.domain.repository.IEntregaDatabasePort;
import br.com.fiapstore.entrega.domain.usecase.IRegistrarAgendamentoEntregaUseCase;
import org.springframework.stereotype.Service;

@Service
public class RegistrarAgendamentoEntrega implements IRegistrarAgendamentoEntregaUseCase {

    private final IEntregaDatabasePort entregaDatabaseAdapter;

    public RegistrarAgendamentoEntrega(IEntregaDatabasePort entregaDatabaseAdapter) {
        this.entregaDatabaseAdapter = entregaDatabaseAdapter;

    }

    @Override
    public EntregaDto executar(EntregaDto entregaDto) {

        Entrega entrega = new Entrega(entregaDto.getCodigoPedido(), entregaDto.getCpf());

        entrega = entregaDatabaseAdapter.save(entrega);

        return EntregaDto.toEntregaDto(entrega);

    }
}
