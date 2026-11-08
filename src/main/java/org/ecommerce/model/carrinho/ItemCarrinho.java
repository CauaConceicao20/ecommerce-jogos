package org.ecommerce.model.carrinho;

import org.ecommerce.model.jogo.Jogo;

import java.time.LocalDateTime;

public class ItemCarrinho {

    private Long id;
    private int quantidade;
    private Jogo jogo;
    private Carrinho carrinho;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public ItemCarrinho() {}

    public ItemCarrinho(int quantidade, Jogo jogo) {
        this.quantidade = quantidade;
        this.jogo = jogo;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public Jogo getJogo() { return jogo; }
    public void setJogo(Jogo jogo) { this.jogo = jogo; }

    public Carrinho getCarrinho() { return carrinho; }
    public void setCarrinho(Carrinho carrinho) { this.carrinho = carrinho; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) { this.dataAtualizacao = dataAtualizacao; }
}
