package org.ecommerce.menu;

import org.ecommerce.model.usuario.Role;
import org.ecommerce.model.usuario.Usuario;
import org.ecommerce.services.AutenticacaoService;
import org.ecommerce.services.RoleService;
import org.ecommerce.services.UsuarioService;

import java.util.Scanner;
import java.util.Set;

public class OperacoesUsuario {

    private final Scanner scanner;
    private final UsuarioService usuarioService = new UsuarioService();
    private final RoleService roleService = new RoleService();
    private final AutenticacaoService autenticacaoService;

    public OperacoesUsuario(Scanner scanner, AutenticacaoService autenticacaoService) {
        this.scanner = scanner;
        this.autenticacaoService = autenticacaoService;
    }

    public void login() {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        autenticacaoService.login(email, senha);

        if (autenticacaoService.retornaUsuarioLogado() != null) {
            System.out.println("Bem-vindo, " + autenticacaoService.retornaUsuarioLogado().getNome() + "!");
        } else {
            System.out.println("Email ou senha inválidos.");
        }
    }

    public void cadastrarUsuario() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.print("Telefone: ");
        String telefone = scanner.nextLine();

        Set<Role> roles = roleService.listarTodas();
        System.out.println("\n=== Perfis ===");
        roles.forEach(r -> System.out.println(r.getId() + " - " + r.getNome()));

        System.out.print("\nCódigo do perfil: ");
        long roleId = Long.parseLong(scanner.nextLine());

        Role role = null;
        for (Role r : roles) {
            if (r.getId().equals(roleId)) {
                role = r;
                break;
            }
        }

        if (role == null) {
            System.out.println("Perfil não encontrado.");
            return;
        }

        Usuario usuario = new Usuario(nome, email, senha, telefone, role);
        Usuario salvo = usuarioService.salvar(usuario);
        System.out.println("Usuário cadastrado com ID: " + salvo.getId());
    }
}
