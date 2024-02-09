package br.com.fiapstore.cobranca.application.usecase;

import br.com.fiapstore.cobranca.application.dto.PagamentoDto;
import br.com.fiapstore.cobranca.domain.entity.Pagamento;
import br.com.fiapstore.cobranca.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.cobranca.domain.exception.PagamentoNaoEncontradoException;
import br.com.fiapstore.cobranca.domain.repository.IPagamentoDatabasePort;
import br.com.fiapstore.cobranca.domain.usecase.ICancelarPagamentoUseCase;
import org.springframework.stereotype.Service;

@Service
public class CancelarPagamento implements ICancelarPagamentoUseCase {

    private final IPagamentoDatabasePort pagamentoDatabasePort;

    public CancelarPagamento(IPagamentoDatabasePort pagamentoDatabasePort) {
        this.pagamentoDatabasePort = pagamentoDatabasePort;
    }


    @Override
    public PagamentoDto executar(String codigoPagamento) throws PagamentoNaoEncontradoException, OperacaoInvalidaException {
        Pagamento pagamento = null;

        pagamento = this.pagamentoDatabasePort.findByCodigo(codigoPagamento);

        if (pagamento == null) throw new PagamentoNaoEncontradoException("Pagamento não encontrado");

        pagamento.cancelar();

        pagamento = this.pagamentoDatabasePort.save(pagamento);

        return PagamentoDto.toPagamentoDto(pagamento);
    }
}
