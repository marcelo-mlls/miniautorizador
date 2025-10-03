# Mini Autorizador

Este é um projeto de um mini autorizador de transações.

## Tecnologias Utilizadas

*   **Java 21**: Versão do Java.
*   **Spring Boot 3**: Framework de desenvolvimento da aplicação.
*   **Maven**: Gerenciador de dependências.
*   **Swagger**: Documentação da API.
*   **MySQL**: Banco de dados relacional para persistência dos dados dos cartões.
*   **JPA (Hibernate)**: Framework de persistência.
*   **Docker & Docker Compose**: Para a execução da aplicação e do banco de dados em contêineres.

## Como Executar a Aplicação

1.  **Pré-requisitos**:
    *   Docker e Docker Compose instalados.

2.  **Inicie a aplicação e o banco de dados**:
    Na raiz do projeto, execute o seguinte comando:
```bash
docker compose up -d --build
```
Este comando fará o seguinte:
*   Construirá a imagem Docker da aplicação a partir do `Dockerfile`.
*   Iniciará um contêiner para a aplicação.
*   Iniciará um contêiner para o banco de dados MySQL.
*   Colocará ambos os contêineres na mesma rede, permitindo que a aplicação se conecte ao banco.

Para parar todos os serviços, execute:
```bash
docker compose down
```

## Autenticação

A API utiliza autenticação **Basic**. As credenciais padrão estão configuradas no arquivo `application.properties` e são:
*   **Username**: `username`
*   **Password**: `password`

## Endpoints da API

### Criar Novo Cartão

*   **Method**: `POST`
*   **URL**: `/cartoes`
*   **Body (JSON)**:
    ```json
    {
        "numeroCartao": "6549873025634501",
        "senha": "1234"
    }
    ```
*   **Respostas**:
    *   **201 Created**: Cartão criado com sucesso.
        ```json
        {
            "numeroCartao": "6549873025634501",
            "senha": "1234"
        }
        ```
    *   **422 Unprocessable Entity**: O cartão já existe.
        ```json
        {
            "numeroCartao": "6549873025634501",
            "senha": "1234"
        }
        ```

### Obter Saldo do Cartão

*   **Method**: `GET`
*   **URL**: `/cartoes/{numeroCartao}`
*   **Respostas**:
    *   **200 OK**: Saldo do cartão.
        ```
        500.00
        ```
    *   **404 Not Found**: Cartão não encontrado.

### Realizar um débito no cartão

*   **Method**: `POST`
*   **URL**: `/transacoes`
*   **Body (JSON)**:
    ```json
    {
        "numeroCartao": "6549873025634501",
        "senhaCartao": "1234",
        "valor": 10.00
    }
    ```
*   **Respostas**:
    *   **201 Created**: Transação realizada com sucesso.
        ```
        OK
        ```
    *   **422 Unprocessable Entity**: Falha na autorização. O corpo da resposta conterá um dos seguintes motivos:
        *   `SALDO_INSUFICIENTE`
        *   `SENHA_INVALIDA`
        *   `CARTAO_INEXISTENTE`

## Acesso via Swagger

A documentação completa da API, incluindo todos os endpoints, está disponível via Swagger UI. Após iniciar a aplicação, acesse:

    http://localhost:8080/swagger-ui/index.html


## Testes Automatizados

O projeto possui testes unitários e de integração. Para executá-los, utilize o Maven Wrapper:

```bash
  ./mvnw test
```

### Tratamento de Concorrência

Para garantir a consistência dos dados em um ambiente com múltiplas transações concorrentes, foi utilizada a estratégia de **locking pessimista** (`PESSIMISTIC_WRITE`) do JPA.

Quando uma transação de débito é iniciada, a linha do cartão correspondente no banco de dados é bloqueada. Qualquer outra transação que tente ler ou modificar o mesmo cartão ficará em espera até que a primeira transação seja concluída (commit ou rollback). Isso evita condições de corrida, como duas transações debitando o mesmo saldo simultaneamente.

### "Sem ifs"

O desafio de construir a solução sem o uso de `if` foi abordado utilizando construções da programação funcional e o sistema de exceções do Java:

*   **`Optional.ifPresent()`**: Para verificar a existência de um cartão e lançar uma exceção caso ele já exista.
*   **`Optional.orElseThrow()`**: Para obter um objeto `Optional` ou lançar uma exceção caso ele esteja vazio.
*   **`Predicate`**: Para encapsular a lógica de validação de senha e saldo. O resultado do predicado é usado para decidir se uma exceção deve ser lançada, evitando um `if` explícito.
*   **Exceções**: O fluxo de controle para cenários de erro é gerenciado através de exceções customizadas, que são capturadas por um `GlobalExceptionHandler`.




