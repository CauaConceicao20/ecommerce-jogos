package org.ecommerce.services;

import org.ecommerce.dao.JogoDao;
import org.ecommerce.model.jogo.Jogo;

import java.sql.SQLException;

public class JogoService {

    private final JogoDao jogoDao = new JogoDao();

    public Jogo salvar(Jogo jogo) {
        return jogoDao.inserir(jogo);
    }

    public int verificarEstoque(Long jogoId) {
        return jogoDao.verificarEstoque(jogoId);
    }

    public void reduzirEstoque(Long jogoId, int quantidade) {
        jogoDao.reduzirEstoque(jogoId, quantidade);
    }
}
