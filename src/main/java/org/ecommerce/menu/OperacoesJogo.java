package org.ecommerce.menu;

import org.ecommerce.model.jogo.Categoria;
import org.ecommerce.model.jogo.Jogo;
import org.ecommerce.services.CategoriaService;
import org.ecommerce.services.JogoService;

import java.util.Scanner;
import java.util.Set;

public class OperacoesJogo {

    private final Scanner scanner;
    private final JogoService jogoService = new JogoService();
    private final CategoriaService categoriaService = new CategoriaService();

    public OperacoesJogo(Scanner scanner) {
        this.scanner = scanner;
    }

    public void cadastrarJogo() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        System.out.print("Preço: ");
        double preco = Double.parseDouble(scanner.nextLine());

        System.out.print("Estoque: ");
        int estoque = Integer.parseInt(scanner.nextLine());

        Set<Categoria> categorias = categoriaService.listarTodas();
        System.out.println("\n=== Categorias ===");
        categorias.forEach(c -> System.out.println(c.getId() + " - " + c.getNome()));

        System.out.print("\nCódigo da categoria: ");
        long categoriaId = Long.parseLong(scanner.nextLine());

        Categoria categoria = null;
        for (Categoria c : categorias) {
            if (c.getId().equals(categoriaId)) {
                categoria = c;
                break;
            }
        }

        if (categoria == null) {
            System.out.println("Categoria não encontrada.");
            return;
        }

        Jogo jogo = new Jogo(nome, descricao, preco, estoque);
        jogo.setCategoria(categoria);
        Jogo salvo = jogoService.salvar(jogo);
        System.out.println("Jogo cadastrado com ID: " + salvo.getId());
    }
}
