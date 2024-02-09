package br.com.fiapstore.cobranca.domain.messaging;


public interface IPagamentoQueueOutPort {
    void publishPagamentoPendente(String pagamentoJson);

    void publishPagamentoConfirmado(String pagamentoJson);

}
