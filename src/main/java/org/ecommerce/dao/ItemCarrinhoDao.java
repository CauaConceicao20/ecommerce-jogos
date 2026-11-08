
package org.ecommerce.dao;

import org.ecommerce.config.ConexaoBancoDados;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemCarrinhoDao {

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
}
