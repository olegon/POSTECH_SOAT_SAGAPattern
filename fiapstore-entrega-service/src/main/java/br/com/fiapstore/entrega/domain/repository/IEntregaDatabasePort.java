package br.com.fiapstore.entrega.domain.repository;

import br.com.fiapstore.entrega.domain.entity.Entrega;

public interface IEntregaDatabasePort {
    Entrega save(Entrega entrega);

    Entrega findByCodigoPedido(String codigo);

    Entrega findByCodigo(String codigo);

}
