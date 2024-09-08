
# Golden Raspberry Awards API

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-green)
![Maven](https://img.shields.io/badge/Maven-3.8.1-orange)
![H2](https://img.shields.io/badge/Database-H2-purple)

## Descrição

Este projeto é uma API RESTful desenvolvida em **Spring Boot** para consultar a lista de nomeados e vencedores da categoria **Pior Filme** do **Golden Raspberry Awards**. O principal objetivo é calcular os produtores com o maior e menor intervalo entre duas vitórias.

A aplicação também permite a importação de dados de filmes através de arquivos CSV, o que facilita a atualização e expansão da base de dados.

## Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.3.3**
- **Maven 3.8.1**
- **H2 Database** para persistência de dados em memória
- **Flyway** para migração de banco de dados
- **JUnit 5** e **Mockito** para testes unitários e de integração

## Funcionalidades

- Consultar produtores com o menor e maior intervalo entre vitórias na categoria "Pior Filme".
- Importar dados de filmes de um arquivo CSV.
- Suporte a testes de integração para validar o comportamento da aplicação.
- Arquitetura modular baseada nos princípios de Clean Architecture.

## Endpoints

### Consultar Intervalos de Vitórias

- **GET** `/v1/award/min-max-intervals`
  
  Exemplo de resposta:
  ```json
  {
    "min": [
      {
        "producer": "Joel Silver",
        "interval": 1,
        "previousWin": 1990,
        "followingWin": 1991
      }
    ],
    "max": [
      {
        "producer": "Matthew Vaughn",
        "interval": 13,
        "previousWin": 2002,
        "followingWin": 2015
      }
    ]
  }
  ```

## Pré-requisitos

- **Java 21** ou superior
- **Maven 3.8.1** ou superior
- **H2 Database** (configuração padrão para testes e execução local)

## Instalação e Execução

### 1. Clonar o repositório

```bash
git clone https://github.com/mathhdpg/Golden-Raspberry-Awards.git
cd Golden-Raspberry-Awards
```

### 2. Configurar o Banco de Dados

O projeto está configurado para utilizar o **banco de dados H2** automaticamente em ambiente local, sem a necessidade de configurações adicionais. O **Flyway** é utilizado para migrações de banco de dados.

Se você deseja alterar o banco de dados para outra solução, como **PostgreSQL**, será necessário ajustar o arquivo `src/main/resources/application.yml` com as credenciais e URL do novo banco.

### 3. Executar a Aplicação

Para rodar a aplicação em ambiente local, execute o seguinte comando:

```bash
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080`

### 4. Importar Dados CSV

O arquivo de dados CSV, localizado em `src/main/resources/data/movielist.csv`, contém a lista de filmes que podem ser alterados. O sistema automaticamente lê esse arquivo e atualiza os dados no banco de dados.

Se você quiser modificar o conteúdo, basta substituir o arquivo CSV e reiniciar a aplicação. A importação será feita automaticamente no **startup**.

Estrutura esperada do CSV:

```csv
year;title;studios;producers;winner
1980;Can't Stop the Music;Associated Film Distribution;Allan Carr;yes
1980;Cruising;Lorimar Productions, United Artists;Jerry Weintraub and Allan Carr;
```

## Testes

O projeto contém testes de unidade e de integração. Os testes de integração garantem que as funcionalidades principais estão funcionando corretamente com o banco de dados.

### Executar todos os testes

Para rodar todos os testes, utilize o comando abaixo:

```bash
mvn test
```

### Testes de Integração

Os testes de integração estão localizados no pacote `com.mg.gra.integration`. Estes testes validam o comportamento da API e dos serviços com o banco de dados H2.

Exemplo de teste de integração:

- `ImportMoviesFromCsvTest`: Valida a importação de filmes a partir de arquivos CSV.
- `AwardControllerTest`: Testa os endpoints para garantir que a API está retornando os dados corretamente.
- `AwardServiceTest`: Valida a lógica de negócio relacionada ao cálculo dos intervalos de prêmios para os produtores.

## Estrutura do Projeto

```bash
src
├── main
│   ├── java
│   │   └── com.mg.gra
│   │       ├── adapters          # Controladores (APIs REST)
│   │       ├── application       # Casos de uso, serviços, DTOs e mapeadores
│   │       ├── domain            # Modelos e gateways
│   │       ├── infrastructure    # Repositórios, implementações e configuração
│   │       └── GraApplication.java  # Ponto de entrada da aplicação
│   └── resources
│       ├── application.yml       # Configurações do Spring Boot
│       ├── data                  # Arquivo CSV de dados
│       └── db                    # Migrações de banco de dados Flyway
└── test
    ├── java
    │   └── com.mg.gra
    │       ├── integration       # Testes de integração
    │       └── unit              # Testes unitários
    └── resources
        ├── sql                   # Scripts SQL para cenários de testes
        └── application-test.yml   # Configurações para testes
```

## Contribuindo

Contribuições são bem-vindas! Sinta-se à vontade para abrir **Issues** e enviar **Pull Requests**.

## Licença

Este projeto é licenciado sob a licença MIT - consulte o arquivo [LICENSE](LICENSE) para mais detalhes.
