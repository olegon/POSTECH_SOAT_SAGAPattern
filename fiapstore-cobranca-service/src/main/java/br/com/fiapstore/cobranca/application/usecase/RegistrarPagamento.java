package br.com.fiapstore.cobranca.application.usecase;

import br.com.fiapstore.cobranca.application.dto.PagamentoDto;
import br.com.fiapstore.cobranca.domain.entity.Pagamento;
import br.com.fiapstore.cobranca.domain.repository.IPagamentoDatabasePort;
import br.com.fiapstore.cobranca.domain.messaging.IPagamentoQueueOutPort;
import br.com.fiapstore.cobranca.domain.usecase.IRegistrarPagamentoUseCase;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
public class RegistrarPagamento implements IRegistrarPagamentoUseCase {
    @Autowired
    private IPagamentoDatabasePort pagamentoDatabasePort;

    @Autowired
    private IPagamentoQueueOutPort pagamentoQueueOutPort;

    @Autowired
    private Gson gson;

    @Transactional
    public PagamentoDto executar(PagamentoDto pagamentoDto) {
        Pagamento pagamento = new Pagamento(pagamentoDto.getCodigoPedido(), pagamentoDto.getValor(), pagamentoDto.getPercentualDesconto(), pagamentoDto.getCpf());
        pagamento = pagamentoDatabasePort.save(pagamento);
        pagamentoQueueOutPort.publishPagamentoPendente(toMessage(pagamento));
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
