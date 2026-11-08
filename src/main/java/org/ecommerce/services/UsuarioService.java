package org.ecommerce.services;

import org.ecommerce.dao.UsuarioDao;
import org.ecommerce.model.usuario.Usuario;

import java.util.Set;

public class UsuarioService {

    private final UsuarioDao usuarioDao = new UsuarioDao();

    public Usuario salvar(Usuario usuario) {
        return usuarioDao.inserirUsuarioComCarrinho(usuario);
    }

    public Set<Usuario> listarTodos() {
        return usuarioDao.listarTodos();
    }
}
