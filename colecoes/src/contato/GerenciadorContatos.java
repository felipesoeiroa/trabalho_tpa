package contato;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import colecao.IColecao;
import listaencadeada.ListaEncadeada;

/**
 * Programa de exemplo/teste da biblioteca de lista encadeada (ListaEncadeada).
 *
 * Mantém os contatos em DUAS listas (IColecao<Contato>): uma comparando por
 * nome e outra comparando por telefone. Isso permite pesquisar de forma
 * eficiente nos dois sentidos, mas exige que toda inserção/remoção seja
 * refletida nas duas listas ao mesmo tempo, para que elas nunca fiquem
 * dessincronizadas (nenhum contato duplicado, nenhuma lista "esquecida").
 *
 * Observação de projeto: como a interface IColecao não oferece iteração nem
 * um método de remoção por referência/identidade, a lista por nome usa um
 * comparador baseado somente no nome. Isso significa que, no caso raro de
 * dois contatos com nomes exatamente iguais, a remoção/pesquisa por nome
 * pode não distinguir qual dos dois é qual — a mesma ambiguidade que o
 * próprio enunciado já assume para a pesquisa por nome.
 *
 * Nenhum método da biblioteca (ListaEncadeada) imprime nada na tela: toda a
 * interação com o usuário acontece aqui.
 */
public class GerenciadorContatos {

    private static final String ARQUIVO_ENTRADA_PADRAO = "entrada.txt";

    private static IColecao<Contato> listaPorNome;
    private static IColecao<Contato> listaPorTelefone;
    private static String arquivoEntrada = ARQUIVO_ENTRADA_PADRAO;

    public static void main(String[] args) {
        // Permite indicar outro arquivo de entrada por argumento de linha de
        // comando (útil para os testes empíricos da Etapa 3, com arquivos de
        // tamanhos diferentes), sem precisar sobrescrever o entrada.txt padrão.
        if (args.length > 0) {
            arquivoEntrada = args[0];
        }

        Scanner scanner = new Scanner(System.in);

        boolean ehOrdenada = perguntarSeOrdenada(scanner);
        listaPorNome = new ListaEncadeada<Contato>(new ComparadorContatoPorNome(), ehOrdenada);
        listaPorTelefone = new ListaEncadeada<Contato>(new ComparadorContatoPorTelefone(), ehOrdenada);

        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcaoMenu(scanner);
            switch (opcao) {
                case 1:
                    carregarDadosDeArquivo();
                    break;
                case 2:
                    adicionarContato(scanner);
                    break;
                case 3:
                    pesquisarPorNome(scanner);
                    break;
                case 4:
                    pesquisarPorTelefone(scanner);
                    break;
                case 5:
                    removerPorTelefone(scanner);
                    break;
                case 6:
                    alterarContato(scanner);
                    break;
                case 7:
                    System.out.println("Quantidade total de contatos: " + listaPorTelefone.quantidadeNos());
                    System.out.println("Encerrando...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 7);

        scanner.close();
    }

    // ---------------------------------------------------------------
    // Menu / entrada de dados
    // ---------------------------------------------------------------

    private static boolean perguntarSeOrdenada(Scanner scanner) {
        int opcao;
        do {
            System.out.println("A lista de contatos deve ser ORDENADA ou NÃO-ORDENADA?");
            System.out.println("1 - Ordenada");
            System.out.println("2 - Não-ordenada");
            System.out.print("Escolha uma opção: ");
            opcao = lerOpcaoMenu(scanner);
            if (opcao != 1 && opcao != 2) {
                System.out.println("Opção inválida!");
            }
        } while (opcao != 1 && opcao != 2);
        return opcao == 1;
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("===== AGENDA DE CONTATOS =====");
        System.out.println("1 - Carregar dados de arquivo");
        System.out.println("2 - Adicionar contato");
        System.out.println("3 - Pesquisar contato por nome");
        System.out.println("4 - Pesquisar contato por telefone");
        System.out.println("5 - Remover contato por telefone");
        System.out.println("6 - Alterar dados de contato");
        System.out.println("7 - Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static int lerOpcaoMenu(Scanner scanner) {
        while (true) {
            String entrada = scanner.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.print("Digite um número válido: ");
            }
        }
    }

    // ---------------------------------------------------------------
    // Operações de negócio
    // ---------------------------------------------------------------

    /**
     * Insere um contato nas duas listas, respeitando a regra de negócio de
     * não permitir telefones duplicados. Essa regra vive aqui, no programa,
     * e não dentro da ListaEncadeada.
     *
     * @return true se o contato foi inserido, false se já existia telefone igual.
     */
    private static boolean inserirContato(Contato contato) {
        Contato existente = listaPorTelefone.pesquisar(new Contato(null, contato.getTelefone()));
        if (existente != null) {
            return false;
        }
        listaPorNome.adicionar(contato);
        listaPorTelefone.adicionar(contato);
        return true;
    }

    private static void carregarDadosDeArquivo() {
        long inicio = System.nanoTime();
        int totalLidos = 0;
        int totalIgnorados = 0;

        try (BufferedReader leitor = new BufferedReader(new FileReader(arquivoEntrada))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty()) {
                    continue;
                }
                // Formato esperado de cada linha: nome;telefone
                String[] partes = linha.split(";", 2);
                if (partes.length < 2) {
                    totalIgnorados++;
                    continue;
                }
                String nome = partes[0].trim();
                String telefone = partes[1].trim();
                if (inserirContato(new Contato(nome, telefone))) {
                    totalLidos++;
                } else {
                    totalIgnorados++;
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo '" + arquivoEntrada + "': " + e.getMessage());
            return;
        }

        long fim = System.nanoTime();
        System.out.println(totalLidos + " contato(s) carregado(s) com sucesso.");
        if (totalIgnorados > 0) {
            System.out.println(totalIgnorados + " linha(s) ignorada(s) (telefone duplicado ou formato inválido).");
        }
        System.out.printf("Tempo total de leitura do arquivo e montagem das listas: %.3f ms%n",
                (fim - inicio) / 1_000_000.0);
    }

    private static void adicionarContato(Scanner scanner) {
        System.out.print("Nome do contato: ");
        String nome = scanner.nextLine().trim();
        System.out.print("Telefone do contato: ");
        String telefone = scanner.nextLine().trim();

        if (inserirContato(new Contato(nome, telefone))) {
            System.out.println("Contato adicionado com sucesso!");
        } else {
            System.out.println("Já existe um contato com o telefone " + telefone + ". Contato não adicionado.");
        }
    }

    private static void pesquisarPorNome(Scanner scanner) {
        System.out.print("Nome do contato a ser pesquisado: ");
        String nome = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato encontrado = listaPorNome.pesquisar(new Contato(nome, null));
        long fim = System.nanoTime();

        if (encontrado == null) {
            System.out.println("Contato não existe.");
        } else {
            System.out.println("Telefone: " + encontrado.getTelefone());
        }
        imprimirTempo("Tempo de busca", fim - inicio);
    }

    private static void pesquisarPorTelefone(Scanner scanner) {
        System.out.print("Telefone do contato a ser pesquisado: ");
        String telefone = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato encontrado = listaPorTelefone.pesquisar(new Contato(null, telefone));
        long fim = System.nanoTime();

        if (encontrado == null) {
            System.out.println("Contato não existe.");
        } else {
            System.out.println("Nome: " + encontrado.getNome());
        }
        imprimirTempo("Tempo de busca", fim - inicio);
    }

    private static void removerPorTelefone(Scanner scanner) {
        System.out.print("Telefone do contato a ser removido: ");
        String telefone = scanner.nextLine().trim();

        long inicio = System.nanoTime();
        Contato encontrado = listaPorTelefone.pesquisar(new Contato(null, telefone));
        boolean removido = false;
        if (encontrado != null) {
            listaPorTelefone.remover(encontrado);
            listaPorNome.remover(encontrado);
            removido = true;
        }
        long fim = System.nanoTime();

        System.out.println(removido ? "Contato removido com sucesso." : "Contato não existe.");
        imprimirTempo("Tempo de remoção", fim - inicio);
    }

    private static void alterarContato(Scanner scanner) {
        System.out.print("Nome do contato a ser alterado: ");
        String nome = scanner.nextLine().trim();

        Contato encontrado = listaPorNome.pesquisar(new Contato(nome, null));
        if (encontrado == null) {
            System.out.println("Contato não existe.");
            return;
        }
        System.out.println("Telefone atual: " + encontrado.getTelefone());

        System.out.print("Novo nome: ");
        String novoNome = scanner.nextLine().trim();
        System.out.print("Novo telefone: ");
        String novoTelefone = scanner.nextLine().trim();

        boolean telefoneMudou = !novoTelefone.equals(encontrado.getTelefone());
        if (telefoneMudou && listaPorTelefone.pesquisar(new Contato(null, novoTelefone)) != null) {
            System.out.println("Já existe outro contato com o telefone " + novoTelefone + ". Alteração cancelada.");
            return;
        }

        // Remove com os dados antigos, atualiza e reinsere: necessário para que
        // as listas ordenadas continuem corretamente ordenadas após a alteração.
        listaPorNome.remover(encontrado);
        listaPorTelefone.remover(encontrado);

        encontrado.setNome(novoNome);
        encontrado.setTelefone(novoTelefone);

        listaPorNome.adicionar(encontrado);
        listaPorTelefone.adicionar(encontrado);

        System.out.println("Contato atualizado com sucesso!");
    }

    private static void imprimirTempo(String rotulo, long nanos) {
        System.out.printf("%s: %.3f ms (%d ns)%n", rotulo, nanos / 1_000_000.0, nanos);
    }
}
