package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.jogo.Jogo;
import org.ecommerce.model.pedido.ItemPedido;
import org.ecommerce.model.pedido.Pedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PedidoDao {

    private ItemPedidoDao itemPedidoDao = new ItemPedidoDao();

    public Pedido criarPedidoCompleto(Pedido pedido, List<ItemPedido> itemPedidos) {
        Connection conn = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            conn.setAutoCommit(false);

            inserir(pedido, conn);

            for (ItemPedido item : itemPedidos) {
                itemPedidoDao.inserir(item, conn);
            }

            conn.commit();
            return pedido;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return null;
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public Pedido inserir(Pedido pedido, Connection conn) throws SQLException {
        String sql = "INSERT INTO public.orders (quantity, total, user_id, status_id) VALUES (?, ?, ?, ?)";

        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, pedido.getQuantidade());
            pstm.setDouble(2, pedido.getTotal());
            pstm.setLong(3, pedido.getUsuario().getId());
            pstm.setLong(4, pedido.getStatus().getId());
            pstm.execute();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) pedido.setId(rs.getLong(1));

            return pedido;
        } finally {
            if (pstm != null) pstm.close();
        }
    }
}
