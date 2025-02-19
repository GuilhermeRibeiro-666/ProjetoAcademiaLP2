import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.Scanner;

class Aluno {
    int id;
    String nome;
    String dataNascimento;
    String status;

    public Aluno(int id, String nome, String dataNascimento, String status) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.status = status;
    }

    public boolean isAtivo() {
        return this.status.equalsIgnoreCase("ativo");
    }
}

class Instrutor {
    int id;
    String nome;
    String especialidade;

    public Instrutor(int id, String nome, String especialidade) {
        this.id = id;
        this.nome = nome;
        this.especialidade = especialidade;
    }
}

class Aula {
    int id;
    String descricao;
    int instrutorId;

    public Aula(int id, String descricao, int instrutorId) {
        this.id = id;
        this.descricao = descricao;
        this.instrutorId = instrutorId;
    }
}

public class Academia {
    private static final String URL = "jdbc:mysql://localhost:3306/academia";
    private static final String USER = "root"; // Substitua pelo seu usuário do MySQL
    private static final String PASSWORD = ""; // Substitua pela sua senha do MySQL

    private static final String ALUNOS_DIR = "alunos";
    private static final String INSTRUTORES_DIR = "instrutores";
    private static final String AULAS_DIR = "aulas";

    public static void main(String[] args) {
        criarPastas();

        Scanner in = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.println("Conexão com o banco de dados estabelecida com sucesso!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            return; // Encerra o programa se a conexão falhar
        }

        while (true) {
            System.out.println("\n1. Cadastrar Aluno \n2. Cadastrar Instrutor \n3. Cadastrar Aula \n4. Alterar Status de Aluno \n5. Cadastrar Aluno em Aula \n6. Listar Alunos \n7. Listar Instrutores \n8. Listar Aulas \n9. Sair");
            int opcao = 0;
            try {
                opcao = in.nextInt();
                in.nextLine(); // Consumir a quebra de linha
            } catch (Exception e) {
                System.out.println("Entrada inválida! Por favor, insira um número.");
                in.nextLine(); // Limpar o buffer do scanner
                continue;
            }

            switch (opcao) {
                case 1:
                    cadastrarAluno(in);
                    break;
                case 2:
                    cadastrarInstrutor(in);
                    break;
                case 3:
                    cadastrarAula(in);
                    break;
                case 4:
                    alterarStatusAluno(in);
                    break;
                case 5:
                    cadastrarAlunoEmAula(in);
                    break;
                case 6:
                    listarAlunos();
                    break;
                case 7:
                    listarInstrutores();
                    break;
                case 8:
                    listarAulas();
                    break;
                case 9:
                    System.out.println("Encerrando o programa...");
                    in.close();
                    return;
                default:
                    System.out.println("Opção inválida!!!");
                    break;
            }
        }
    }

    private static void criarPastas() {
        File alunosDir = new File(ALUNOS_DIR);
        if (!alunosDir.exists()) {
            alunosDir.mkdir();
        }

        File instrutoresDir = new File(INSTRUTORES_DIR);
        if (!instrutoresDir.exists()) {
            instrutoresDir.mkdir();
        }

        File aulasDir = new File(AULAS_DIR);
        if (!aulasDir.exists()) {
            aulasDir.mkdir();
        }
    }

    private static void cadastrarAluno(Scanner in) {
        System.out.println("Preencha o campo com o seu nome completo: ");
        String nome = in.nextLine();
        System.out.println("Preencha o campo com a sua data de nascimento (yyyy-MM-dd): ");
        String dataNascimento = in.nextLine();

        String sql = "INSERT INTO Aluno (nome, data_nascimento) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, dataNascimento);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    criarArquivoAluno(id, nome, dataNascimento);
                    System.out.println("Aluno cadastrado com sucesso!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void criarArquivoAluno(int id, String nome, String dataNascimento) {
        String fileName = ALUNOS_DIR + "/aluno_" + id + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("ID: " + id + "\n");
            writer.write("Nome: " + nome + "\n");
            writer.write("Data de Nascimento: " + dataNascimento + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cadastrarInstrutor(Scanner in) {
        System.out.println("Preencha o campo com o seu nome completo: ");
        String nome = in.nextLine();
        System.out.println("Conte-nos a sua especialidade: ");
        String especialidade = in.nextLine();

        String sql = "INSERT INTO Instrutor (nome, especialidade) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, especialidade);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    criarArquivoInstrutor(id, nome, especialidade);
                    System.out.println("Instrutor cadastrado com sucesso!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void criarArquivoInstrutor(int id, String nome, String especialidade) {
        String fileName = INSTRUTORES_DIR + "/instrutor_" + id + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("ID: " + id + "\n");
            writer.write("Nome: " + nome + "\n");
            writer.write("Especialidade: " + especialidade + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cadastrarAula(Scanner in) {
        System.out.println("Preencha o campo com a descrição sobre a aula: ");
        String descricao = in.nextLine();
        listarInstrutores();
        System.out.println("Escolha o ID do instrutor para esta aula: ");
        int instrutorId = in.nextInt();
        in.nextLine(); // Consumir a quebra de linha

        String sql = "INSERT INTO Aula (descricao, instrutor_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, descricao);
            pstmt.setInt(2, instrutorId);
            pstmt.executeUpdate();

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    criarArquivoAula(id, descricao, instrutorId);
                    System.out.println("Aula cadastrada com sucesso!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void criarArquivoAula(int id, String descricao, int instrutorId) {
        String fileName = AULAS_DIR + "/aula_" + id + ".txt";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write("ID: " + id + "\n");
            writer.write("Descrição: " + descricao + "\n");
            writer.write("Instrutor ID: " + instrutorId + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void alterarStatusAluno(Scanner in) {
        listarAlunos();
        System.out.println("Escolha o ID do aluno para alterar seu status: ");
        int alunoId = in.nextInt();
        in.nextLine(); // Consumir a quebra de linha
        System.out.println("Novo status (ativo/inativo): ");
        String status = in.nextLine();

        String sql = "UPDATE Aluno SET status = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, alunoId);
            pstmt.executeUpdate();
            System.out.println("Status do aluno atualizado com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void cadastrarAlunoEmAula(Scanner in) {
        listarAlunos();
        System.out.println("Escolha o ID do aluno: ");
        int alunoId = in.nextInt();
        in.nextLine(); // Consumir a quebra de linha
        listarAulas();
        System.out.println("Escolha o ID da aula: ");
        int aulaId = in.nextInt();
        in.nextLine(); // Consumir a quebra de linha

        String sql = "INSERT INTO Aluno_Aula (aluno_id, aula_id) VALUES (?, ?)";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, alunoId);
            pstmt.setInt(2, aulaId);
            pstmt.executeUpdate();
            System.out.println("Aluno cadastrado na aula com sucesso!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarAlunos() {
        String sql = "SELECT * FROM Aluno";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String dataNascimento = rs.getString("data_nascimento");
                String status = rs.getString("status");
                System.out.println("ID: " + id + ", Nome: " + nome + ", Data de Nascimento: " + dataNascimento + ", Status: " + status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarInstrutores() {
        String sql = "SELECT * FROM Instrutor";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String especialidade = rs.getString("especialidade");
                System.out.println("ID: " + id + ", Nome: " + nome + ", Especialidade: " + especialidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void listarAulas() {
        String sql = "SELECT * FROM Aula";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String descricao = rs.getString("descricao");
                int instrutorId = rs.getInt("instrutor_id");
                System.out.println("ID: " + id + ", Descrição: " + descricao + ", Instrutor ID: " + instrutorId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}