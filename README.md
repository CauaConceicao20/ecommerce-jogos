# E-commerce de Jogos

Projeto de e-commerce de jogos desenvolvido em Java, com banco de dados PostgreSQL.

## Estrutura do projeto

### Já existe

- `src/app/Main.java`: ponto de entrada da aplicação. Atualmente, tenta abrir uma conexão com o banco.
- `src/config/DatabaseConnection.java`: configura e fornece a conexão JDBC com o PostgreSQL.
- `src/lib/postgresql-42.7.14.jar`: driver JDBC do PostgreSQL.

### Planejado

- `src/dao/`: classes para consultar e alterar os dados no banco.
- `src/model/`: classes que representam as entidades do sistema, como jogos e clientes.
- `src/services/`: regras de negócio e operações que conectam a aplicação aos DAOs.

Essa estrutura poderá crescer conforme novas funcionalidades forem adicionadas.
