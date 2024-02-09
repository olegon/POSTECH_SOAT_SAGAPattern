package br.com.fiapstore.pedido.domain.repository;


import br.com.fiapstore.pedido.domain.entity.Pedido;

import java.util.List;

public interface IPedidoDatabaseAdapter {
    Pedido save(Pedido pedido);

    Pedido findByCodigoPedido(String codigo);

    List<Pedido> findAll();
}
