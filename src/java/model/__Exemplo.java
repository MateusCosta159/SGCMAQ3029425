package model;

import java.sql.SQLException;
import java.util.ArrayList;
import model.framework.DataBaseConnections;

public class __Exemplo {

    public static void main(String[] args) throws SQLException {

        // --- Testando a tabela tipo_usuario ---
        ArrayList<TipoUsuario> lstTipo = new TipoUsuario().getAllTableEntities();
        System.out.println("Lista de tipos de usuário:");
        System.out.println(lstTipo);

        // --- Testando a tabela usuarios ---
        Usuario u = new Usuario();

        // Exemplo de inserção
//        u.setId(1);
//        u.setNome("João da Silva");
//        u.setCpf("123.456.789-00");
//        u.setSenha("senha123"); // Ideal seria criptografar
//        u.setTipoUsuarioId(1); // assumindo que já existe um tipo_usuario com id=1
//        u.save(); // insere no banco
//        System.out.println("Usuário inserido: " + u);

        // Exemplo de carregamento (load) pelo ID
//        u.setId(1);
//        boolean status = u.load();
//        System.out.println("Carregado? " + status);
//        System.out.println("Usuário carregado: " + u);

        // Exemplo de atualização
//        u.setNome("João da Silva Atualizado");
//        u.setSenha("novaSenha123");
//        u.save(); // update
//        System.out.println("Usuário atualizado: " + u);

        // Exemplo de exclusão
//        u.delete();
//        System.out.println("Usuário deletado!");

        // Listando todos os usuários
        ArrayList<Usuario> lstUsuarios = new Usuario().getAllTableEntities();
        System.out.println("Lista de usuários:");
        System.out.println(lstUsuarios);
    }
}
