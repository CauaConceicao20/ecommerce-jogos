package org.ecommerce;

import org.ecommerce.config.InicializadorBancoDados;
import org.ecommerce.menu.OperacoesCarrinho;
import org.ecommerce.menu.OperacoesJogo;
import org.ecommerce.menu.OperacoesUsuario;
import org.ecommerce.services.AutenticacaoService;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final AutenticacaoService autenticacaoService = new AutenticacaoService();
    private static final OperacoesUsuario OPERACOES_USUARIO = new OperacoesUsuario(scanner, autenticacaoService);
    private static final OperacoesJogo OPERACOES_JOGO = new OperacoesJogo(scanner);
    private static final OperacoesCarrinho OPERACOES_CARRINHO = new OperacoesCarrinho(scanner);

    public static void main(String[] args) {
        try {
            InicializadorBancoDados.executar();
        } catch (SQLException e) {
            System.err.println("Falha ao inicializar o banco de dados: " + e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Falha ao carregar o script do banco: " + e.getMessage());
            System.exit(1);
        }

        int opcao = 0;
        do {
            boolean logado = autenticacaoService.retornaUsuarioLogado() != null;

            System.out.println("\n=== E-commerce de Jogos ===");

            if (!logado) {
                System.out.println("1. Login");
                System.out.println("2. Cadastrar usuário");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                String entradaDeslogado = scanner.nextLine().trim();
                if (entradaDeslogado.isEmpty()) continue;
                opcao = Integer.parseInt(entradaDeslogado);

                switch (opcao) {
                    case 1 -> OPERACOES_USUARIO.login();
                    case 2 -> OPERACOES_USUARIO.cadastrarUsuario();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida.");
                }
            } else {
                System.out.println("1. Cadastrar jogo");
                System.out.println("2. Colocar jogo no carrinho");
                System.out.println("3. Deslogar");
                System.out.println("0. Sair");
                System.out.print("Opção: ");
                String entradaLogado = scanner.nextLine().trim();
                if (entradaLogado.isEmpty()) continue;
                opcao = Integer.parseInt(entradaLogado);

                switch (opcao) {
                    case 1 -> OPERACOES_JOGO.cadastrarJogo();
                    case 2 -> OPERACOES_CARRINHO.adicionarJogo();
                    case 3 -> autenticacaoService.logout();
                    case 0 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida.");
                }
            }
        } while (opcao != 0);
    }
}
