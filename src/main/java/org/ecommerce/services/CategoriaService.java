package org.ecommerce.services;

import org.ecommerce.dao.CategoriaDao;
import org.ecommerce.model.jogo.Categoria;

import java.util.Set;

public class CategoriaService {

    private final CategoriaDao categoriaDao = new CategoriaDao();

    public Set<Categoria> listarTodas() {
        return categoriaDao.listarTodas();
    }
}
