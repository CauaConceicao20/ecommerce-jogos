
package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;
import org.ecommerce.model.carrinho.ItemCarrinho;
import org.ecommerce.model.jogo.Jogo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemCarrinhoDao {

    public List<ItemCarrinho> listarPorUsuario(Long usuarioId) {
        String sql =
            "SELECT ci.id, ci.quantity, g.id AS game_id, g.name, g.price " +
            "FROM public.cart_items ci " +
            "JOIN public.carts c ON ci.cart_id = c.id " +
            "JOIN public.games g ON ci.game_id = g.id " +
            "WHERE c.user_id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        List<ItemCarrinho> itens = new ArrayList<>();
        try {
            conn = ConexaoBancoDados.obterConexao();
            pstm = conn.prepareStatement(sql);
            pstm.setLong(1, usuarioId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Jogo jogo = new Jogo(rs.getString("name"), null, rs.getDouble("price"), 0);
                jogo.setId(rs.getLong("game_id"));
                ItemCarrinho item = new ItemCarrinho(rs.getInt("quantity"), jogo);
                item.setId(rs.getLong("id"));
                itens.add(item);
            }
            return itens;
        } catch (SQLException e) {
            e.printStackTrace();
            return itens;
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void removerItens(List<Long> itemIds) {
        String sql = "DELETE FROM public.cart_items WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            conn = ConexaoBancoDados.obterConexao();
            conn.setAutoCommit(false);
            pstm = conn.prepareStatement(sql);
            for (Long id : itemIds) {
                pstm.setLong(1, id);
                pstm.addBatch();
            }
            pstm.executeBatch();
            conn.commit();
        } catch (SQLException e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try {
                if (pstm != null) pstm.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public void adicionarJogo(Long usuarioId, Long jogoId, int quantidade)
            throws SQLException {

        if (quantidade <= 0) {
            throw new IllegalArgumentException(
                "A quantidade deve ser maior que zero."
            );
        }

        String sqlCarrinho =
            "SELECT id FROM public.carts WHERE user_id = ?";

        String sqlEstoque =
            "SELECT stock FROM public.games " +
            "WHERE id = ? AND active = TRUE";

        String sqlItem =
            "SELECT id, quantity FROM public.cart_items " +
            "WHERE cart_id = ? AND game_id = ? FOR UPDATE";

        String sqlAtualizar =
            "UPDATE public.cart_items " +
            "SET quantity = ?, updated_at = NOW() WHERE id = ?";

        String sqlInserir =
            "INSERT INTO public.cart_items " +
            "(quantity, cart_id, game_id) VALUES (?, ?, ?)";

        try (Connection conn = ConexaoBancoDados.obterConexao()) {

            conn.setAutoCommit(false);

            try {
                long carrinhoId;

                try (PreparedStatement ps =
                         conn.prepareStatement(sqlCarrinho)) {

                    ps.setLong(1, usuarioId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                "Carrinho do usuário não encontrado."
                            );
                        }

                        carrinhoId = rs.getLong("id");
                    }
                }

                int estoque;

                try (PreparedStatement ps =
                         conn.prepareStatement(sqlEstoque)) {

                    ps.setLong(1, jogoId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            throw new SQLException(
                                "Jogo não encontrado ou inativo."
                            );
                        }

                        estoque = rs.getInt("stock");
                    }
                }

                Long itemId = null;
                int quantidadeAtual = 0;

                try (PreparedStatement ps =
                         conn.prepareStatement(sqlItem)) {

                    ps.setLong(1, carrinhoId);
                    ps.setLong(2, jogoId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            itemId = rs.getLong("id");
                            quantidadeAtual = rs.getInt("quantity");
                        }
                    }
                }

                int novaQuantidade = quantidadeAtual + quantidade;

                if (novaQuantidade > estoque) {
                    throw new SQLException(
                        "Estoque insuficiente. Disponível: " + estoque
                    );
                }

                if (itemId != null) {

                    try (PreparedStatement ps =
                             conn.prepareStatement(sqlAtualizar)) {

                        ps.setInt(1, novaQuantidade);
                        ps.setLong(2, itemId);
                        ps.executeUpdate();
                    }

                    System.out.println(
                        "Quantidade do jogo atualizada no carrinho!"
                    );

                } else {

                    try (PreparedStatement ps =
                             conn.prepareStatement(sqlInserir)) {

                        ps.setInt(1, quantidade);
                        ps.setLong(2, carrinhoId);
                        ps.setLong(3, jogoId);
                        ps.executeUpdate();
                    }

                    System.out.println("Jogo adicionado ao carrinho!");
                }

                conn.commit();

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        }
    }

    //Falta validar se o carrinho está vazio caso vazio mostrar uma mensagem e parar tudo.
    /*
    * Outro ponto é observe que vc cria varios PreparedStatement para cada operação
    * poderia ser um metodo separado deixando esse metodo removerUnidade bem menor
    * e os outros metodos auxiliares reutilizaveis cuidado apenas com a Connection do banco
    * e com o commit automatico com essas dicas aqui vc tmb vai conseguir ver que os metodos
    * acima tmb podem ser diminuidos como exemplo vc pode ver o inserir em ItemPedidoDao fica a dica.
     */
    public void removerUmaUnidade(Long usuarioId, Long jogoId) throws SQLException {
        //Transforme em um metodo e retorne o Item pedido
    String sqlBuscar =
        "SELECT ci.id, ci.quantity " +
        "FROM public.cart_items ci " +
        "JOIN public.carts c ON ci.cart_id = c.id " +
        "WHERE c.user_id = ? AND ci.game_id = ? " +
        "FOR UPDATE";
       //Tambem transforme em um metodo para fazer a redução da quantidade
    String sqlAtualizar =
        "UPDATE public.cart_items " +
        "SET quantity = quantity - 1, updated_at = NOW() " +
        "WHERE id = ?";
       //Aqui tmb transforme em um metodo para exclusão

        /*OBS: Fazendo isso vc vai deixar o metodo mais limpo
        * Tambem vai conseguir reutilizar os metodos em outras operações
        */
    String sqlExcluir =
        "DELETE FROM public.cart_items WHERE id = ?";

    try (Connection conn = ConexaoBancoDados.obterConexao()) {

        conn.setAutoCommit(false);

        try {
            Long itemId = null;
            int quantidade = 0;

            try (PreparedStatement ps = conn.prepareStatement(sqlBuscar)) {

                ps.setLong(1, usuarioId);
                ps.setLong(2, jogoId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        itemId = rs.getLong("id");
                        quantidade = rs.getInt("quantity");
                    }
                }
            }
            if (itemId == null) {
                throw new SQLException("Jogo nao encontrado");
            }
            if (quantidade > 1) {

                try (PreparedStatement ps = conn.prepareStatement(sqlAtualizar)) {
                    ps.setLong(1, itemId);
                    ps.executeUpdate();
                }

                System.out.println("Unidade removida do carrinho");

            } else {
                try (PreparedStatement ps = conn.prepareStatement(sqlExcluir)) {
                    ps.setLong(1, itemId);
                    ps.executeUpdate();
                }

                System.out.println("Jogo removido");
            }

            conn.commit();

        } catch (Exception e) {
            conn.rollback();
            throw e;
        }
    }
    }
}
