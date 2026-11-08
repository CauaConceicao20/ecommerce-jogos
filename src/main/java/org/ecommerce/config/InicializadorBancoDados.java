package org.ecommerce.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

public final class InicializadorBancoDados {
    private InicializadorBancoDados() {
    }

    public static void executar() throws IOException, SQLException {
        InputStream recurso = InicializadorBancoDados.class.getClassLoader()
                .getResourceAsStream("inicializar.sql");

        if (recurso == null) {
            throw new IOException("O recurso inicializar.sql não foi encontrado no classpath.");
        }

        String sql;
        try (InputStream entrada = recurso;
             BufferedReader leitor = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8))) {
            sql = leitor.lines().collect(Collectors.joining("\n"));
        }

        try (Connection conexao = ConexaoBancoDados.obterConexao();
             Statement instrucao = conexao.createStatement()) {
            for (String comando : sql.split(";")) {
                String comandoLimpo = comando.trim();
                if (!comandoLimpo.isEmpty()) {
                    instrucao.execute(comandoLimpo);
                }
            }
        }
    }
}
