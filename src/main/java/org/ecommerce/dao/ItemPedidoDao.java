package org.ecommerce.dao;

import org.ecommerce.model.pedido.ItemPedido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemPedidoDao {

    public void inserir(ItemPedido item, Connection conn) throws SQLException {
        String sql = "INSERT INTO public.order_items (quantity, price, order_id, game_id) VALUES (?, ?, ?, ?)";

        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstm.setInt(1, item.getQuantidade());
            pstm.setDouble(2, item.getPreco());
            pstm.setLong(3, item.getPedido().getId());
            pstm.setLong(4, item.getJogo().getId());
            pstm.execute();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) item.setId(rs.getLong(1));
        } finally {
            if (pstm != null) pstm.close();
        }
    }
}
