package org.ecommerce.services;

import org.ecommerce.model.usuario.Usuario;

public class AutenticacaoService {

    Usuario usuarioLogado = null;
    UsuarioService usuarioService = new UsuarioService();

    public void login(String email, String senha) {
        Usuario usuario = usuarioService.buscarPorEmail(email);

        if(usuario != null) {
            if (usuario.getSenha().equals(senha)) {
                this.usuarioLogado = usuario;
            } else {
                System.out.printf("Usuario não logado");
            }
        }
    }

    public void logout() {
        this.usuarioLogado = null;
        System.out.println("Usúario deslogado com sucesso");
    }

    public Usuario retornaUsuarioLogado() {
        return usuarioLogado;
    }
}
