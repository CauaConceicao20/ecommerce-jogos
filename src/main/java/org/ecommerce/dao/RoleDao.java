package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.usuario.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

public class RoleDao {

    public Role inserir(Role role) {
        String sql = "INSERT INTO public.roles (name, description) VALUES (?, ?)";

        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            pstm.setString(1, role.getNome());
            pstm.setString(2, role.getDescricao());
            pstm.execute();

            ResultSet rs = pstm.getGeneratedKeys();
            if (rs.next()) role.setId(rs.getLong(1));

            return role;
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

    public Set<Role> listarTodas() {
        String sql = "SELECT * FROM public.roles";

        Connection conn = null;
        PreparedStatement pstm = null;
        Set<Role> roles = new HashSet<>();

        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);

            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getLong("id"));
                role.setNome(rs.getString("name"));
                role.setDescricao(rs.getString("description"));
                roles.add(role);
            }
            return roles;
        } catch (Exception e) {
            e.printStackTrace();
            return roles;
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
