package org.ecommerce.model.pedido;

import java.time.LocalDateTime;

public class Status {
    private Long id;
    private StatusPedido nome;
    private String description;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StatusPedido getName() { return nome; }
    public void setName(StatusPedido nome) { this.nome = nome; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
}
