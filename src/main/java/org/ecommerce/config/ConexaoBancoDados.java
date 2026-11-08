package org.ecommerce.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class ConexaoBancoDados {
    private static final String URL = "jdbc:postgresql://localhost:5432/ecommerce_jogos";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "root";

    private ConexaoBancoDados() {}

    public static Connection obterConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
