import java.sql.*;
import java.util.Scanner;

public class BancoDeDadosSimulado {
    static final String URL = "jdbc:sqlite:pessoas.db";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        criarTabela();

        int opcao;
        do {
            System.out.println("\n=== Menu ===");
            System.out.println("1. Adicionar Pessoa");
            System.out.println("2. Listar Pessoas");
            System.out.println("3. Atualizar Pessoa");
            System.out.println("4. Remover Pessoa");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha

            switch (opcao) {
                case 1:
                    adicionarPessoa();
                    break;
                case 2:
                    listarPessoas();
                    break;
                case 3:
                    atualizarPessoa();
                    break;
                case 4:
                    removerPessoa();
                    break;
                case 0:
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }

        } while (opcao != 0);
    }

    // Criação da tabela no banco
    public static void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS pessoas (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nome TEXT NOT NULL," +
                     "idade INTEGER NOT NULL" +
                     ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela: " + e.getMessage());
        }
    }

    // Adiciona uma nova pessoa
    public static void adicionarPessoa() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();

        String sql = "INSERT INTO pessoas (nome, idade) VALUES (?, ?);";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, idade);
            pstmt.executeUpdate();

            System.out.println("Pessoa adicionada com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao adicionar pessoa: " + e.getMessage());
        }
    }

    // Lista todas as pessoas
    public static void listarPessoas() {
        String sql = "SELECT * FROM pessoas;";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean temPessoas = false;
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                int idade = rs.getInt("idade");

                System.out.println("ID: " + id + " | Nome: " + nome + " | Idade: " + idade);
                temPessoas = true;
            }

            if (!temPessoas) {
                System.out.println("Nenhuma pessoa cadastrada.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao listar pessoas: " + e.getMessage());
        }
    }

    // Atualiza os dados de uma pessoa
    public static void atualizarPessoa() {
        System.out.print("Digite o ID da pessoa a ser atualizada: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Novo nome: ");
        String nome = scanner.nextLine();
        System.out.print("Nova idade: ");
        int idade = scanner.nextInt();
        scanner.nextLine();

        String sql = "UPDATE pessoas SET nome = ?, idade = ? WHERE id = ?;";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nome);
            pstmt.setInt(2, idade);
            pstmt.setInt(3, id);

            int linhasAfetadas = pstmt.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Pessoa atualizada com sucesso!");
            } else {
                System.out.println("Pessoa não encontrada.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao atualizar pessoa: " + e.getMessage());
        }
    }

    // Remove uma pessoa do banco
    public static void removerPessoa() {
        System.out.print("Digite o ID da pessoa a ser removida: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        String sql = "DELETE FROM pessoas WHERE id = ?;";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int linhasAfetadas = pstmt.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Pessoa removida com sucesso.");
            } else {
                System.out.println("Pessoa não encontrada.");
            }

        } catch (SQLException e) {
            System.out.println("Erro ao remover pessoa: " + e.getMessage());
        }
    }
}
