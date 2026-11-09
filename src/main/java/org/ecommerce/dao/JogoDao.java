package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.jogo.Jogo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    public int verificarEstoque(Long jogoId) {
        String sql = "SELECT stock FROM public.games WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, jogoId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return rs.getInt("stock");
            throw new SQLException("Jogo não encontrado.");
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void reduzirEstoque(Long jogoId, int quantidade) {
        String sql = "UPDATE public.games SET stock = stock - ? WHERE id = ? AND stock >= ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setInt(1, quantidade);
            pstm.setLong(2, jogoId);
            pstm.setInt(3, quantidade);
            int linhasAfetadas = pstm.executeUpdate();
            if (linhasAfetadas == 0) throw new SQLException("Estoque insuficiente.");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
