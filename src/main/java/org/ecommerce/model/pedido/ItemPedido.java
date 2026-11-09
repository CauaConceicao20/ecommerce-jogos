package org.ecommerce.model.pedido;

import org.ecommerce.model.jogo.Jogo;

public class ItemPedido {

    private Long id;
    private int quantidade;
    private double preco;
    private Pedido pedido;
    private Jogo jogo;

    public ItemPedido() {

    }

    public ItemPedido(Pedido pedido, Jogo jogo, int quantidade) {
        this.pedido = pedido;
        this.jogo = jogo;
        this.quantidade = quantidade;
        this.preco = jogo.getPreco() * quantidade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public Pedido getPedido() { return pedido; }
    public void setPedido(Pedido pedido) { this.pedido = pedido; }
    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }
}
