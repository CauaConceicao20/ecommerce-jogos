
package org.ecommerce.menu;

import org.ecommerce.dao.ItemCarrinhoDao;

import java.sql.SQLException;
import java.util.Scanner;

public class OperacoesCarrinho {

    private final Scanner scanner;
    private final ItemCarrinhoDao itemCarrinhoDao =
        new ItemCarrinhoDao();

    public OperacoesCarrinho(Scanner scanner) {
        this.scanner = scanner;
    }

    public void adicionarJogo() {

        try {
            System.out.print("ID do usuário: ");
            Long usuarioId =
                Long.parseLong(scanner.nextLine());

            System.out.print("ID do jogo: ");
            Long jogoId =
                Long.parseLong(scanner.nextLine());

            System.out.print("Quantidade: ");
            int quantidade =
                Integer.parseInt(scanner.nextLine());

            itemCarrinhoDao.adicionarJogo(
                usuarioId, jogoId, quantidade
            );

        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
