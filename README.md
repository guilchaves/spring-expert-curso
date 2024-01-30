# Projeto Final - Sistema de Catálogo de Produtos

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![JUnit](https://img.shields.io/badge/JUnit-%25A162.svg?style=for-the-badge&logo=junit5&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)

Esse repositório contém o projeto final desenvolvido durante o treinamento [Java Spring Expert](https://devsuperior.com.br/curso-java-spring-expert), da plataforma Dev Superior.</br>

Este projeto visa criar uma API RESTful de um sistema de catágolo de produtos usando tecnologias **Java, Spring Boot Framework, 
Spring Security, Bean Validation, PostgresSQL como banco de dados, JWT para controle de autenticação, JUnit e Mockito para testes unitários e integrados, JaCoCo para cobertura de testes**. </br> 

## Visão Geral do Sistema
O sistema desenvolvido é uma aplicaćão completa de sistema de catálogo de produtos, abrangendo cadastro de usuários, produtos e categorias.
Cada usuário, seja cliente ou administrador, tem sua própria área de interação com o sistema. O sistema permite a consulta de produtos, categorias, usuários 
e alteração de senha a partir de envio de email.

## Funcionalidades Principais
- Cadastro e autenticaćão de usuários com diferentes papéis (cliente, administrador).
- Catálogo de produtos com capacidade de filtragem por nome ou categoria.
- Área administrativa para gerenciamento de usuários, produtos e categorias.
- Recuperação de senha através de requisição e envio de email do sistema para o usuário.

## Como executar este projeto
##### Pré-requisitos:
- **Java 17**: [JDK 17](https://www.oracle.com/java/technologies/downloads/) ou superior.
- **IDEs**: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/) ou [Spring Tools](https://spring.io/tools).

##### Passos:

1. **Clone o repositório**</br>
   Abra o terminal e navegue até o diretório onde deseja armazenar o projeto. Execute o seguinte comando para clonar o repositório:

```bash
git clone https://github.com/guilchaves/spring-professional-dscatalog.git
```
2. **Abra o projeto no IntelliJ IDEA ou STS:**</br>
- _IntelliJ IDEA_: Abra o IntelliJ IDEA e selecione "Open" no menu principal. Navegue até o diretório do projeto e selecione o arquivo pom.xml.
- _Spring Tools Suite_: Abra o STS e selecione "Import...​" > "Existing Maven Projects". Navegue até o diretório do projeto e selecione o arquivo pom.xml.

3. **Baixe as dependências do Maven:**</br>
   Aguarde até que o IntelliJ ou STS baixe automaticamente as dependências do Maven. Isso pode levar algum tempo, dependendo da conexão com a internet.</br></br>
4. **Execute o projeto:**</br>
   No projeto, navegue até o arquivo `src/main/java/br/com/guilchaves/dscatalog/DscatalogApplication.java`. Este arquivo contém
   a classe principal da aplicação Spring Boot.</br>
- _IntelliJ IDEA_: Clique com o botão direito do mouse no arquivo DscatalogApplication.java e escolha "Run DscatalogApplication".
- _Spring Tools Suite_: Clique com o botão direito do mouse no projeto no navegador de projetos e escolha "Run As" > "Spring Boot App".

5. **Verifique a Execução:**</br>
   Após a execução bem-sucedida, abra um navegador da web e acesse `http://localhost:8080` (ou a porta configurada, se diferente) para verificar se a aplicação está em execução.</br>
   Acesse `http://localhost:8080/h2-console` para utilizar o console do H2 database.</br>
 
## Para testar a API no postman
Para importar e exportar dados no postman, consulte a documentação oficial [aqui](https://learning.postman.com/docs/getting-started/importing-and-exporting/importing-data/).
</br>
Download da coleção e variáveis de ambiente:
- [Collection](https://drive.google.com/file/d/17irZncK1W-CPsYR5rguL4BRQzCb9xQJJ/view?usp=sharing)
- [Environment](https://drive.google.com/file/d/1_OgHsNGXs10fS2m7MbD0F0gMUm0sH7H1/view?usp=sharing)

## Endpoints da API:

```
POST /oauth2/token - Autenticação na API.

POST /auth/recover-token - Retorna token para recuperação de senha; envio de email com endereço de recuperação de senha.

PUT /auth/new-password - Atualiza senha de usuário (requer Token válido).

GET /users - Retorna lista de usuários (requer privilégio ADMIN).

GET /users/{id} - Retorna usuário por id (requer privilégio ADMIN).

POST /users - Cadastra usuário com privilégio OPERATOR.

PUT /users - Atualiza dados do usuário (requer privilégio ADMIN).

DELETE /users/{id} - Deleta usuário por id (requer privilégio ADMIN).

GET /me - Retorna dados do usuário logado (requer login).

GET /categories - Retorna lista de categorias.

GET /categories/{id} - Retorna categoria por id.

POST /categories - Insere nova categoria (requer privilégio admin)

PUT /categories/{id} - Atualiza categoria por id (requer privilégio ADMIN).

DELETE /categories/{id} - Deleta categoria por id (requer privilégio ADMIN).

GET /products - Retorna lista de produtos.

GET /products/{id} - Retorna produto por id.

POST /products - Adiciona novo produto (requer privilégio ADMIN).

PUT /products/{id} - Atualiza produto por id (requer privilégio ADMIN).

DELETE /products/{id} - Deleta produto por id (requer privilégio ADMIN).
```

## Autenticação
A API utiliza o Spring Security para controle de autenticação. Os seguintes papéis estão disponíveis:
```
OPERATOR -> Papel padrão para usuários autenticados.</br>
ADMIN -> Papel de administrador para gerenciar produtos (adicionar, atualiza, remove produtos). 
```
Para acessar os endpoints protegidos como um usuário ADMIN, forneça as credenciais de autenticação adequadas no cabeçalho da requisição.

