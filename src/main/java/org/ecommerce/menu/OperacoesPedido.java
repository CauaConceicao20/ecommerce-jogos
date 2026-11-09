package org.ecommerce.menu;

import org.ecommerce.dao.ItemCarrinhoDao;
import org.ecommerce.dao.PedidoDao;
import org.ecommerce.dao.StatusDao;
import org.ecommerce.model.carrinho.ItemCarrinho;
import org.ecommerce.model.pedido.ItemPedido;
import org.ecommerce.model.pedido.Pedido;
import org.ecommerce.model.pedido.Status;
import org.ecommerce.model.usuario.Usuario;
import org.ecommerce.services.AutenticacaoService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class OperacoesPedido {

    private final Scanner scanner;
    private final ItemCarrinhoDao itemCarrinhoDao = new ItemCarrinhoDao();
    private final PedidoDao pedidoDao = new PedidoDao();
    private final StatusDao statusDao = new StatusDao();
    private final AutenticacaoService autenticacaoService;

    public OperacoesPedido(Scanner scanner, AutenticacaoService autenticacaoService) {
        this.scanner = scanner;
        this.autenticacaoService = autenticacaoService;
    }

    public void fazerPedido() {
        Usuario usuario = autenticacaoService.retornaUsuarioLogado();
        List<ItemCarrinho> itensCarrinho = itemCarrinhoDao.listarPorUsuario(usuario.getId());

        if (itensCarrinho.isEmpty()) {
            System.out.println("Seu carrinho está vazio.");
            return;
        }

        System.out.println("\n=== Itens do Carrinho ===");
        System.out.printf("%-5s %-25s %-10s %-10s%n", "ID", "Nome", "Qtd", "Preço");
        for (ItemCarrinho item : itensCarrinho) {
            System.out.printf("%-5d %-25s %-10d R$ %.2f%n",
                item.getId(),
                item.getJogo().getNome(),
                item.getQuantidade(),
                item.getJogo().getPreco());
        }

        List<ItemPedido> itensPedido = new ArrayList<>();
        List<Long> idsRemover = new ArrayList<>();

        System.out.println("\nDigite o ID do item para adicionar ao pedido (0 para finalizar):");
        while (true) {
            System.out.print("ID: ");
            String entrada = scanner.nextLine().trim();
            if (entrada.isEmpty()) continue;

            Long idSelecionado = Long.parseLong(entrada);
            if (idSelecionado == 0) break;

            ItemCarrinho selecionado = itensCarrinho.stream()
                .filter(i -> i.getId().equals(idSelecionado))
                .findFirst().orElse(null);

            if (selecionado == null) {
                System.out.println("ID não encontrado.");
                continue;
            }

            if (idsRemover.contains(idSelecionado)) {
                System.out.println("Item já adicionado ao pedido.");
                continue;
            }

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setQuantidade(selecionado.getQuantidade());
            itemPedido.setPreco(selecionado.getJogo().getPreco());
            itemPedido.setJogo(selecionado.getJogo());
            itensPedido.add(itemPedido);
            idsRemover.add(idSelecionado);
            System.out.println("\"" + selecionado.getJogo().getNome() + "\" adicionado ao pedido.");
        }

        if (itensPedido.isEmpty()) {
            System.out.println("Nenhum item selecionado.");
            return;
        }

        Status statusPendente = statusDao.buscarPorNome("PENDENTE");
        if (statusPendente == null) {
            System.out.println("Erro: status PENDENTE não encontrado.");
            return;
        }

        int quantidadeTotal = itensPedido.stream().mapToInt(ItemPedido::getQuantidade).sum();
        double totalPedido = itensPedido.stream()
            .mapToDouble(i -> i.getPreco() * i.getQuantidade()).sum();

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setStatus(statusPendente);
        pedido.setQuantidade(quantidadeTotal);
        pedido.setTotal(totalPedido);

        for (ItemPedido item : itensPedido) {
            item.setPedido(pedido);
        }

        Pedido criado = pedidoDao.criarPedidoCompleto(pedido, itensPedido);
        if (criado == null) {
            System.out.println("Erro ao criar pedido.");
            return;
        }

        itemCarrinhoDao.removerItens(idsRemover);
        System.out.printf("Pedido #%d criado com sucesso! Total: R$ %.2f%n", criado.getId(), totalPedido);
    }
}
