package org.ecommerce.services;

import org.ecommerce.dao.PedidoDao;
import org.ecommerce.model.pedido.ItemPedido;
import org.ecommerce.model.pedido.Pedido;

import java.util.List;

public class PedidoService {

    private PedidoDao pedidoDao = new PedidoDao();

    public Pedido criarPedido(Pedido pedido, List<ItemPedido> itens) {
        return pedidoDao.criarPedidoCompleto(pedido, itens);
    }

}
