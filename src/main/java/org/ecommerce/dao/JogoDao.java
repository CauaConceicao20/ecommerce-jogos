package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.jogo.Jogo;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class JogoDao {

    public Jogo inserir(Jogo jogo) {
        String sql = "INSERT INTO public.games (name, description, price, stock, category_id) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstm.setString(1, jogo.getNome());
            pstm.setString(2, jogo.getDescricao());
            pstm.setDouble(3, jogo.getPreco());
            pstm.setInt(4, jogo.getEstoque());
            pstm.setObject(5, jogo.getCategoria().getId());
            pstm.execute();

            var rs = pstm.getGeneratedKeys();
            if (rs.next()) jogo.setId(rs.getLong(1));

            return jogo;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
