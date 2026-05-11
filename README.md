# Projeto de Qualidade TADS Senac 

Projeto educacional com objetivo de aplicarmos conceitos aprendidos em aula, com instruções do professor Afonso Lelis.
O projeto consiste em um sistema de cadastro e manutenção de livros, com interface Web. Ele foi desenvolvido em java, utilizando Spring Boot e Maven para Back End, MongoDB para banco de dados e ThymeLeaf e BootStrap para Front End.


## Funcionalidades

- Cadastro e gerenciamento de livros
- Interface web usando Thymeleaf e BootStrap
- Banco de dados MongoDB para armazenamento de livros

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.0
- Maven
- Thymeleaf
- Bootstrap
- Banco de Dados MongoDB
- Spring MVC
- Spring Security
- Spring Validation
- Spring Data MongoDB
- Lombok
- JUnit 5
- Docker
- Spring Security Test

## Conexão ao Banco de Dados

Este projeto utiliza um banco de dados MongoDB local, com porta padrão 27017

## Começando

### Pré-requisitos
- Java JDK 17 ou superior
- Maven 3.8.0 ou superior
- Docker Desktop

### Instalação do Maven

#### No Windows:
1. Baixe o Maven do site oficial: https://maven.apache.org/download.cgi
2. Extraia o arquivo ZIP para um diretório como `C:\apache-maven-3.9.5`
3. Adicione a variável de ambiente `MAVEN_HOME` apontando para o diretório do Maven
4. Adicione `%MAVEN_HOME%\bin` ao seu PATH
5. Verifique a instalação com o comando:
```cmd
mvn -version
```


### Executando a Aplicação

1. Clone o repositório
git clone https://github.com/Bloodborne2/ProjetoQA.git
cd ProjetoQA
   
2. Navegue até o diretório do projeto
   
3. Execute a aplicação usando Maven:

```bash
./mvnw spring-boot:run
```

Ou construa e execute o JAR:
```bash
mvnw.cmd clean package
java -jar target/ProjetoQA-0.0.1-SNAPSHOT.jar
```

### Comandos úteis do Maven

- Para compilar o projeto: `mvn compile`
- Para executar os testes: `mvn test`
- Para empacotar o projeto: `mvn package`
- Para limpar os arquivos gerados: `mvn clean`
- Para executar a aplicação: `mvn spring-boot:run`

### Acessando a Aplicação

Após iniciar a aplicação, você pode acessá-la em:
- Página de login: http://localhost:8080/login
- Página de cadastro: http://localhost:8080/cadastro
- Página de livros: http://localhost:8080/livros
- Página de cadastro de livro: http://localhost:8080/livros/novo

### API Endpoints

A aplicação também disponibiliza endpoints RESTful para integração com outras aplicações:

#### Livros
- `GET /api/livros` - Listar todos os livros do usuário autenticado
- `GET /api/livros/{id}` - Obter detalhes de um livro específico
- `POST /api/livros` - Cadastrar um novo livro para o usuário
- `PUT /api/livros/{id}` - Atualizar informações de um livro existente
- `DELETE /api/livros/{id}` - Remover um livro do acervo

## Estrutura do Projeto

- `model/` - Entidades MongoDB para Livro e Usuário
- `dto/` – Objetos de transferência para entrada e saída de dados, garantindo a segurança e validação das informações sem expor as entidades de banco
- `repository/` - Interfaces da camada de acesso a dados
- `service/` - Camada de lógica de negócios
- `exception/` – Centralização do tratamento de erros para retornar mensagens padronizadas e códigos HTTP adequados
- `controller/` - Controladores web
- `security/` – Configurações de autenticação e autorização do Spring Security
- `templates/` - Templates Thymeleaf com Bootstrap

## Valor Educacional

Este projeto demonstra:
- Capacidade de aplicar temas debatidos em sala na prática
- Operações CRUD básicas
- Arquitetura MVC
- Fundamentos do Spring Boot
- Uso do Spring Data MongoDB
- Injeção de dependência
- Design de API RESTful
- Integração com banco de dados NoSQL
- Template frontend com Thymeleaf
