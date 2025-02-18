import java.util.Scanner;
import java.util.ArrayList;

class Aluno {
    String nome;
    String dataNascimento;
    String status;
    ArrayList<String> aulasDesejadas; // Lista de aulas que o aluno deseja fazer

    public Aluno(String nome, String dataNascimento) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.status = "ativo"; // Status padrão definido como "ativo" no cadastro
        this.aulasDesejadas = new ArrayList<>();
    }

    // Método para verificar se o aluno está ativo
    public boolean isAtivo() {
        return this.status.equalsIgnoreCase("ativo");
    }

    // Método para adicionar uma aula desejada pelo aluno
    public void adicionarAulaDesejada(String aula) {
        this.aulasDesejadas.add(aula);
        System.out.println("Aula " + aula + " adicionada às preferências do aluno " + this.nome + ".");
    }

    // Método para verificar se o aluno deseja fazer uma aula específica
    public boolean desejaFazerAula(String aula) {
        return this.aulasDesejadas.contains(aula);
    }
}

class Instrutor {
    String nomeIns;
    String especialidade;

    public Instrutor(String nomeIns, String especialidade) {
        this.nomeIns = nomeIns;
        this.especialidade = especialidade;
    }
}

class Aula {
    String descricao;
    ArrayList<Aluno> alunosPresentes; // Lista de alunos presentes na aula
    Instrutor instrutorPresente; // Instrutor responsável pela aula

    public Aula(String descricao, Instrutor instrutorPresente) {
        this.descricao = descricao;
        this.instrutorPresente = instrutorPresente;
        this.alunosPresentes = new ArrayList<>();
    }

    // Método para adicionar um aluno à aula
    public void adicionarAluno(Aluno aluno) {
        if (aluno.isAtivo() && aluno.desejaFazerAula(this.descricao)) {
            alunosPresentes.add(aluno);
            System.out.println("Aluno " + aluno.nome + " adicionado à aula de " + descricao + ".");
        } else if (!aluno.isAtivo()) {
            System.out.println("O aluno " + aluno.nome + " está inativo e não pode ser adicionado à aula.");
        } else {
            System.out.println("O aluno " + aluno.nome + " não deseja fazer esta aula.");
        }
    }
}

// Nova classe para marcar aula e verificar se o aluno está ativo ou inativo
class MarcadorAula {
    // Método para verificar se a descrição da aula corresponde à especialidade do professor
    public boolean verificarCompatibilidadeAulaProfessor(String descricaoAula, Instrutor instrutor) {
        return descricaoAula.toLowerCase().contains(instrutor.especialidade.toLowerCase());
    }
}

public class Academia {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ArrayList<Aluno> alunos = new ArrayList<>();
        ArrayList<Instrutor> instrutores = new ArrayList<>();
        ArrayList<Aula> aulas = new ArrayList<>();
        MarcadorAula marcadorAula = new MarcadorAula();

        // Menu
        while (true) {
            System.out.println("\n1. Cadastrar Aluno \n2. Cadastrar Instrutor \n3. Cadastrar Aula \n4. Alterar Status de Aluno \n5. Cadastrar Aluno em Aula \n6. Marcar Aula para um Aluno \n7. Sair \n8. Voltar");
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
                    System.out.println("Preencha o campo com o seu nome completo: ");
                    String nome = in.nextLine();
                    // Validar se o nome não é uma data
                    if (nome.matches(".*\\d.*")) {
                        System.out.println("Nome inválido! O nome não pode conter números.");
                        break;
                    }
                    System.out.println("Preencha o campo com a sua data de nascimento (dd/MM/yyyy): ");
                    String dataNascimento = in.nextLine();
                    // Validar a data de nascimento
                    if (!dataNascimento.matches("\\d{2}/\\d{2}/\\d{4}")) {
                        System.out.println("Data de nascimento inválida! Use o formato dd/MM/yyyy.");
                        break;
                    }
                    Aluno aluno = new Aluno(nome, dataNascimento);
                    System.out.println("Aluno cadastrado com status ativo!");

                    // Perguntar ao aluno qual aula ele deseja fazer
                    System.out.println("Digite o nome da aula que deseja fazer (ou 'sair' para finalizar): ");
                    while (true) {
                        String aulaDesejada = in.nextLine();
                        if (aulaDesejada.equalsIgnoreCase("sair")) {
                            break;
                        }
                        aluno.adicionarAulaDesejada(aulaDesejada);
                        System.out.println("Digite outra aula que deseja fazer (ou 'sair' para finalizar): ");
                    }
                    alunos.add(aluno);
                    break;
                case 2:
                    System.out.println("Preencha o campo com o seu nome completo: ");
                    String nomeIns = in.nextLine();
                    // Validar se o nome não é uma data
                    if (nomeIns.matches(".*\\d.*")) {
                        System.out.println("Nome inválido! O nome não pode conter números.");
                        break;
                    }
                    System.out.println("Conte-nos a sua especialidade");
                    String especialidade = in.nextLine();
                    Instrutor instrutor = new Instrutor(nomeIns, especialidade);
                    System.out.println("Instrutor cadastrado!!");
                    instrutores.add(instrutor);
                    break;
                case 3:
                    if (instrutores.isEmpty()) {
                        System.out.println("Nenhum instrutor cadastrado. Cadastre um instrutor primeiro.");
                        break;
                    }
                    System.out.println("Preencha o campo com a descrição sobre a aula");
                    String descricao = in.nextLine();
                    // Listar instrutores e escolher um
                    System.out.println("Escolha o instrutor para esta aula:");
                    for (int i = 0; i < instrutores.size(); i++) {
                        System.out.println(i + ". " + instrutores.get(i).nomeIns + " - Especialidade: " + instrutores.get(i).especialidade);
                    }
                    int instrutorIndex = -1;
                    try {
                        instrutorIndex = in.nextInt();
                        in.nextLine(); // Consumir a quebra de linha
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Por favor, insira um número.");
                        in.nextLine(); // Limpar o buffer do scanner
                        break;
                    }
                    if (instrutorIndex < 0 || instrutorIndex >= instrutores.size()) {
                        System.out.println("Índice de instrutor inválido!");
                        break;
                    }
                    // Verifique se a descrição da aula corresponde à especialidade do professor
                    Instrutor instrutorEscolhido = instrutores.get(instrutorIndex);
                    if (!marcadorAula.verificarCompatibilidadeAulaProfessor(descricao, instrutorEscolhido)) {
                        System.out.println("A descrição da aula não corresponde à especialidade do professor. Escolha outro professor ou altere a descrição da aula.");
                        break;
                    }
                    Aula aula = new Aula(descricao, instrutorEscolhido);
                    aulas.add(aula);
                    System.out.println("Aula cadastrada com o instrutor: " + instrutorEscolhido.nomeIns);
                    break;
                case 4:
                    if (alunos.isEmpty()) {
                        System.out.println("Nenhum aluno cadastrado.");
                        break;
                    }
                    System.out.println("Selecione o aluno para alterar seu status");
                    for (int i = 0; i < alunos.size(); i++) {
                        System.out.println(i + ". " + alunos.get(i).nome + " - Status: " + alunos.get(i).status);
                    }
                    int alunoIndex = -1;
                    try {
                        alunoIndex = in.nextInt();
                        in.nextLine(); // Consumir a quebra de linha
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Por favor, insira um número.");
                        in.nextLine(); // Limpar o buffer do scanner
                        break;
                    }
                    if (alunoIndex < 0 || alunoIndex >= alunos.size()) {
                        System.out.println("Índice de aluno inválido!");
                        break;
                    }
                    System.out.println("Novo status (ativo/inativo): ");
                    String novoStatus = in.nextLine();
                    if (!novoStatus.equalsIgnoreCase("ativo") && !novoStatus.equalsIgnoreCase("inativo")) {
                        System.out.println("Status inválido! O status deve ser 'ativo' ou 'inativo'.");
                        break;
                    }
                    alunos.get(alunoIndex).status = novoStatus;
                    System.out.println("Status atualizado!");
                    break;
                case 5:
                    if (alunos.isEmpty() || aulas.isEmpty()) {
                        System.out.println("Nenhum aluno ou aula cadastrada.");
                        break;
                    }
                    System.out.println("Selecione o aluno para cadastrar em uma aula:");
                    for (int i = 0; i < alunos.size(); i++) {
                        System.out.println(i + ". " + alunos.get(i).nome + " - Status: " + alunos.get(i).status);
                    }
                    int alunoCadastroIndex = -1;
                    try {
                        alunoCadastroIndex = in.nextInt();
                        in.nextLine(); // Consumir a quebra de linha
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Por favor, insira um número.");
                        in.nextLine(); // Limpar o buffer do scanner
                        break;
                    }
                    if (alunoCadastroIndex < 0 || alunoCadastroIndex >= alunos.size()) {
                        System.out.println("Índice de aluno inválido!");
                        break;
                    }
                    System.out.println("Selecione uma aula para cadastrar o aluno:");
                    for (int i = 0; i < aulas.size(); i++) {
                        System.out.println(i + ". " + aulas.get(i).descricao + " - Instrutor: " + aulas.get(i).instrutorPresente.nomeIns);
                    }
                    int aulaCadastroIndex = -1;
                    try {
                        aulaCadastroIndex = in.nextInt();
                        in.nextLine(); // Consumir a quebra de linha
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Por favor, insira um número.");
                        in.nextLine(); // Limpar o buffer do scanner
                        break;
                    }
                    if (aulaCadastroIndex < 0 || aulaCadastroIndex >= aulas.size()) {
                        System.out.println("Índice de aula inválida!");
                        break;
                    }
                    aulas.get(aulaCadastroIndex).adicionarAluno(alunos.get(alunoCadastroIndex));
                    break;
                case 6:
                    if (alunos.isEmpty() || aulas.isEmpty()) {
                        System.out.println("Nenhum aluno ou aula cadastrada.");
                        break;
                    }
                    System.out.println("Selecione o aluno para marcar a aula:");
                    for (int i = 0; i < alunos.size(); i++) {
                        System.out.println(i + ". " + alunos.get(i).nome + " - Status: " + alunos.get(i).status);
                    }
                    int alunoEscolhido = -1;
                    try {
                        alunoEscolhido = in.nextInt();
                        in.nextLine(); // Consumir a quebra de linha
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Por favor, insira um número.");
                        in.nextLine(); // Limpar o buffer do scanner
                        break;
                    }
                    if (alunoEscolhido < 0 || alunoEscolhido >= alunos.size()) {
                        System.out.println("Índice de aluno inválido!");
                        break;
                    }
                    System.out.println("Selecione uma aula para o aluno:");
                    for (int i = 0; i < aulas.size(); i++) {
                        System.out.println(i + ". " + aulas.get(i).descricao + " - Instrutor: " + aulas.get(i).instrutorPresente.nomeIns);
                    }
                    int aulaEscolhida = -1;
                    try {
                        aulaEscolhida = in.nextInt();
                        in.nextLine(); // Consumir a quebra de linha
                    } catch (Exception e) {
                        System.out.println("Entrada inválida! Por favor, insira um número.");
                        in.nextLine(); // Limpar o buffer do scanner
                        break;
                    }
                    if (aulaEscolhida < 0 || aulaEscolhida >= aulas.size()) {
                        System.out.println("Índice de aula inválida!");
                        break;
                    }
                    aulas.get(aulaEscolhida).adicionarAluno(alunos.get(alunoEscolhido));
                    break;
                case 7:
                    System.out.println("Encerrando o programa...");
                    in.close();
                    return;
                case 8:
                    // Voltar uma página (neste caso, apenas continua o loop)
                    System.out.println("Voltando ao menu anterior...");
                    break;
                default:
                    System.out.println("Opção inválida!!!");
                    break;
            }
        }
    }

    // Função para contar alunos ativos
    public static void contarAlunosAtivos(ArrayList<Aluno> alunos) {
        int contador = 0;
        for (Aluno aluno : alunos) {
            if (aluno.isAtivo()) {
                contador++;
                System.out.println("Aluno: " + aluno.nome + " - Status: Ativo");
            }
        }
        System.out.println("Total de alunos ativos: " + contador);
    }

    // Função para contar instrutores
    public static void contarInstrutores(ArrayList<Instrutor> instrutores) {
        int contador = 0;
        for (Instrutor instrutor : instrutores) {
            contador++;
            System.out.println("Instrutor: " + instrutor.nomeIns + " - Especialidade: " + instrutor.especialidade);
        }
        System.out.println("Total de instrutores: " + contador);
    }
}