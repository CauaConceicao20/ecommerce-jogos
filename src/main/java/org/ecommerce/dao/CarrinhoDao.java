package org.ecommerce.dao;

import org.ecommerce.model.carrinho.Carrinho;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CarrinhoDao {

    public Carrinho inserir(Carrinho carrinho, Connection conn) throws SQLException {
        String sql = "INSERT INTO public.carts (user_id) VALUES (?)";

        PreparedStatement pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstm.setLong(1, carrinho.getUsuario().getId());
        pstm.execute();

        ResultSet rs = pstm.getGeneratedKeys();
        if (rs.next()) carrinho.setId(rs.getLong(1));

        return carrinho;
    }
}
