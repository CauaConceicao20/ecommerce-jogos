package org.ecommerce.services;

import org.ecommerce.dao.JogoDao;
import org.ecommerce.model.jogo.Jogo;

public class JogoService {

    private JogoDao jogoDao = new JogoDao();

    public Jogo salvar(Jogo jogo) {
        return jogoDao.inserir(jogo);
    }
}
