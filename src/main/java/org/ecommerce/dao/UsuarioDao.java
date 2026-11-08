package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.carrinho.Carrinho;
import org.ecommerce.model.usuario.Role;
import org.ecommerce.model.usuario.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class UsuarioDao {

    private final CarrinhoDao carrinhoDao = new CarrinhoDao();

    public Usuario inserirUsuarioComCarrinho(Usuario usuario) {
        Connection conn = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            conn.setAutoCommit(false);

            inserir(usuario, conn);

            Carrinho carrinho = new Carrinho(usuario);
            carrinhoDao.inserir(carrinho, conn);

            conn.commit();
            return usuario;
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return null;
        } finally {
            try { if (conn != null) conn.close(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public Usuario inserir(Usuario usuario, Connection conn) throws SQLException {
        String sql = "INSERT INTO public.users (name, email, password, phone, role_id) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
        pstm.setString(1, usuario.getNome());
        pstm.setString(2, usuario.getEmail());
        pstm.setString(3, usuario.getSenha());
        pstm.setString(4, usuario.getTelefone());
        pstm.setLong(5, usuario.getRole().getId());
        pstm.execute();

        ResultSet rs = pstm.getGeneratedKeys();
        if (rs.next()) usuario.setId(rs.getLong(1));

        return usuario;
    }

    public Set<Usuario> listarTodos() {
        String sql =
                "SELECT u.*, r.name AS role_name, r.description AS role_description" +
                " FROM public.users u " +
                "JOIN public.roles r " +
                "ON u.role_id = r.id";

        Connection conn = null;
        PreparedStatement pstm = null;
        Set<Usuario> usuarios = new HashSet<>();

        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("role_id"));
                role.setNome(rs.getString("role_name"));
                role.setDescricao(rs.getString("role_description"));

                Usuario usuario = new Usuario();
                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("name"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("password"));
                usuario.setTelefone(rs.getString("phone"));
                usuario.setAtivo(rs.getBoolean("active"));
                usuario.setRole(role);
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (Exception e) {
            e.printStackTrace();
            return usuarios;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Usuario buscaPorEmail(String email) {
        String sql = "SELECT u.*, r.name AS role_name, r.description AS role_description" +
                     " FROM public.users u JOIN public.roles r ON u.role_id = r.id" +
                     " WHERE u.email = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        Usuario usuario = new Usuario();
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, email);


            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("role_id"));
                role.setNome(rs.getString("role_name"));
                role.setDescricao(rs.getString("role_description"));

                usuario.setId(rs.getLong("id"));
                usuario.setNome(rs.getString("name"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("password"));
                usuario.setTelefone(rs.getString("phone"));
                usuario.setAtivo(rs.getBoolean("active"));
                usuario.setRole(role);
                return usuario;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return usuario;
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
