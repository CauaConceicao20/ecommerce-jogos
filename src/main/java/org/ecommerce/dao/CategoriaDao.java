package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.jogo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class CategoriaDao {

    public Categoria inserir(Categoria categoria) {
        String sql = "INSERT INTO CATEGORIA (nome, descricao) VALUES (?,?)";

        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstm.setString(1, categoria.getNome());
            pstm.setString(2, categoria.getDescricao());
            pstm.execute();

            return categoria;
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

    public Set<Categoria> listarTodas() {
        String sql = "SELECT * FROM public.categories";

        Connection conn = null;
        PreparedStatement pstm = null;
        Set<Categoria> categorias = new HashSet<>();

        try{
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);

            ResultSet rset = pstm.executeQuery();

            while(rset.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rset.getLong("id"));
                categoria.setNome(rset.getString("name"));
                categoria.setDescricao(rset.getString("description"));
                categorias.add(categoria);
            }
            return categorias;
        } catch (Exception e) {
            e.printStackTrace();
            return categorias;
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
