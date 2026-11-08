
package org.ecommerce.menu;

import org.ecommerce.dao.ItemCarrinhoDao;
import org.ecommerce.services.AutenticacaoService;

import java.sql.SQLException;
import java.util.Scanner;

public class OperacoesCarrinho {

    private final Scanner scanner;
    private final ItemCarrinhoDao itemCarrinhoDao =
        new ItemCarrinhoDao();
    private final AutenticacaoService autenticacaoService = new AutenticacaoService();

    public OperacoesCarrinho(Scanner scanner) {
        this.scanner = scanner;
    }

    public void adicionarJogo() {

        try {
            Long usuarioId = autenticacaoService.retornaUsuarioLogado().getId();

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
