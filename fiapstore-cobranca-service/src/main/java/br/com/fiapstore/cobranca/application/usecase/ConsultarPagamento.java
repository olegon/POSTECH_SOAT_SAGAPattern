package br.com.fiapstore.cobranca.application.usecase;

import br.com.fiapstore.cobranca.application.dto.PagamentoDto;
import br.com.fiapstore.cobranca.domain.entity.Pagamento;
import br.com.fiapstore.cobranca.domain.exception.PagamentoNaoEncontradoException;
import br.com.fiapstore.cobranca.domain.repository.IPagamentoDatabasePort;
import br.com.fiapstore.cobranca.domain.usecase.IConsultarPagamentoUseCase;
import org.springframework.stereotype.Service;

@Service
public class ConsultarPagamento implements IConsultarPagamentoUseCase {

    private final IPagamentoDatabasePort pagamentoDatabasePort;

    public ConsultarPagamento(IPagamentoDatabasePort pagamentoDatabasePort) {
        this.pagamentoDatabasePort = pagamentoDatabasePort;
    }


    @Override
    public PagamentoDto executar(String codigo) throws PagamentoNaoEncontradoException {
        Pagamento pagamento;

        pagamento = pagamentoDatabasePort.findByCodigo(codigo);

        if (pagamento == null) throw new PagamentoNaoEncontradoException("Pagamento não encontrado");

        return PagamentoDto.toPagamentoDto(pagamento);
    }
}
