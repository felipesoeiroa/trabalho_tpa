# 📚 Disciplina: Técnicas de Programação Avançadas (2026)

Este repositório contém a implementação do trabalho em Java, utilizando dos conhecimentos de **Estruturas de Dados e Programação Orientada a Objetos**. Foi utilizada uma biblioteca de listas encadeadas genéricas (`ListaEncadeada<T>`) e dois programas de exemplo que a utilizam.

---

## 📂 Organização do Repositório

Todo o código-fonte está na pasta `colecoes/src`, organizada por pacote:

* **`colecao`** — interface `IColecao<T>`, fornecida para utilizar no trabalho, que toda estrutura de dados do trabalho deve implementar (`adicionar`, `pesquisar`, `remover`, `quantidadeNos`).
* **`listaencadeada`** — a biblioteca implementada no Item 1 do trabalho:
  * `No<T>`: nó genérico da lista (valor + ponteiro para o próximo).
  * `ListaEncadeada<T>`: implementação de `IColecao<T>` como lista simplesmente encadeada. Recebe no construtor um `Comparator<T>` e um `boolean` indicando se a lista deve se manter ordenada. Não imprime nenhuma mensagem, toda interação com o usuário fica a cargo dos programas que a utilizam.
* **`dominio`** — exemplo de uso fornecido pelo professor: classe `Aluno`, `ComparadorAlunoPorMatricula` e `Main` (cadastro simples de alunos por matrícula).
* **`contato`** — programa do Item 2 do trabalho: uma agenda de contatos que usa a `ListaEncadeada`.
  * `Contato`: classe de domínio (`nome`, `telefone`), com `toString()` no formato `"nome-telefone"`.
  * `ComparadorContatoPorNome` / `ComparadorContatoPorTelefone`: comparadores usados para manter/pesquisar duas instâncias de `IColecao<Contato>` — uma indexada por nome, outra por telefone.
  * `GerenciadorContatos`: classe com `main`, contendo o menu interativo e a medição do tempo de execução das operações de carga, busca e remoção.
* **`util`** — `GeradorEntrada`: utilitário para gerar arquivos de contatos aleatórios de qualquer tamanho, usado nos testes empíricos de complexidade (Etapa 3 do relatório). Uso: `java -cp bin util.GeradorEntrada <quantidade> <arquivo_saida> [seed]`.
* **`colecoes/entrada.txt`** — arquivo de exemplo usado pela opção "Carregar dados de arquivo" do `GerenciadorContatos`. Formato: uma linha por contato, `nome;telefone`.
* **`colecoes/dados_teste/`** — arquivos grandes gerados por `GeradorEntrada` e usados nos testes empíricos.

---

## 🛠️ Como rodar os exemplos no VS Code

Para garantir que a IDE reconheça o projeto corretamente, siga estes passos:

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/nome-do-repo.git
    ```
2.  **Abra o VS Code.**
3.  Vá em `File > Open Folder...` e selecione a pasta **`colecoes`**.
    > **Importante:** Não abra a pasta raiz do repositório se quiser que o suporte ao Java (IntelliSense) funcione perfeitamente.
4.  Certifique-se de ter o **Extension Pack for Java** da Microsoft instalado.
5.  Abra `dominio/Main.java` (exemplo de alunos) ou `contato/GerenciadorContatos.java` (agenda de contatos) e clique em **Run** acima do método `main`.

---

## 💻 Comandos Úteis (Terminal)

Caso prefira rodar via terminal sem usar o botão "Run" da IDE:

Rode os comandos abaixo a partir da pasta `colecoes` (é onde fica o `entrada.txt` usado pelo programa de contatos).

**Para compilar:**
* No Linux/Mac:
  ```bash
  javac $(find src -name "*.java") -d bin
  ```
* No Windows (PowerShell):
  ```powershell
  javac (Get-ChildItem -Recurse src/*.java) -d bin
  ```

**Para executar o exemplo de alunos (fornecido pelo professor):**
```
java -cp bin dominio.Main
```

**Para executar a agenda de contatos (Item 2 do trabalho):**
```
java -cp bin contato.GerenciadorContatos
```
