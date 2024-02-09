package br.com.fiapstore.cobranca.application.usecase;

import br.com.fiapstore.cobranca.application.dto.PagamentoDto;
import br.com.fiapstore.cobranca.domain.entity.Pagamento;
import br.com.fiapstore.cobranca.domain.exception.OperacaoInvalidaException;
import br.com.fiapstore.cobranca.domain.exception.PagamentoNaoEncontradoException;
import br.com.fiapstore.cobranca.domain.repository.IPagamentoDatabasePort;
import br.com.fiapstore.cobranca.domain.messaging.IPagamentoQueueOutPort;
import br.com.fiapstore.cobranca.domain.usecase.IConfirmarPagamentoUseCase;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ConfirmarPagamento implements IConfirmarPagamentoUseCase {
    @Autowired
    private IPagamentoDatabasePort pagamentoDatabasePort;

    @Autowired
    private IPagamentoQueueOutPort pagamentoQueueOutPort;

    @Autowired
    private Gson gson;

    public PagamentoDto executar(String codigoPagamento) throws PagamentoNaoEncontradoException, OperacaoInvalidaException {
        Pagamento pagamento = null;

        pagamento = this.pagamentoDatabasePort.findByCodigo(codigoPagamento);

        if (pagamento == null) throw new PagamentoNaoEncontradoException("Pagamento não encontrado");

        pagamento.confirmar();

        pagamento = this.pagamentoDatabasePort.save(pagamento);

        this.pagamentoQueueOutPort.publishPagamentoConfirmado(toMessage(pagamento));

        return PagamentoDto.toPagamentoDto(pagamento);
    }

    public String toMessage(Pagamento pagamento) {
        var message = new HashMap<String, Object>();
        message.put("codigoPagamento", pagamento.getCodigo());
        message.put("codigoPedido", pagamento.getCodigoPedido());
        message.put("cpf", pagamento.getCpf());
        return gson.toJson(message);
    }
}
