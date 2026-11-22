package org.ecommerce.menu;

import org.ecommerce.dao.ItemCarrinhoDao;
import org.ecommerce.services.AutenticacaoService;

import java.sql.SQLException;
import java.util.Scanner;

public class OperacoesCarrinho {

    private final Scanner scanner;
    private final ItemCarrinhoDao itemCarrinhoDao = new ItemCarrinhoDao();
    private final AutenticacaoService autenticacaoService;

    public OperacoesCarrinho(Scanner scanner, AutenticacaoService autenticacaoService) {
        this.scanner = scanner;
        this.autenticacaoService = autenticacaoService;
    }

    public void adicionarJogo() {
        try {
            Long usuarioId = autenticacaoService.retornaUsuarioLogado().getId();

            System.out.print("ID do jogo: ");
            Long jogoId = Long.parseLong(scanner.nextLine());

            System.out.print("Quantidade: ");
            int quantidade = Integer.parseInt(scanner.nextLine());

            itemCarrinhoDao.adicionarJogo(usuarioId, jogoId, quantidade);
        } catch (SQLException | IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    /*
    Faça a listagem dos items que estão no carrinho atualmente para que seja
    possivel ver qual item queremos solucionar se vazio informa e para a operação
     */
public void removerJogo() {
    try {
        Long usuarioId = autenticacaoService.retornaUsuarioLogado().getId();

        System.out.print("ID do jogo que deseja remover: ");
        Long jogoId = Long.parseLong(scanner.nextLine());

        itemCarrinhoDao.removerUmaUnidade(usuarioId, jogoId);

    } catch (SQLException | IllegalArgumentException e) {
        System.out.println("Erro: " + e.getMessage());
    }
}


}
