# 💰 Fintech API - Solução de Gestão Financeira Pessoal

Bem-vindo à **Fintech API**, um ecossistema robusto e escalável construído com **Spring Boot 3** e **Oracle Database** para controle financeiro completo. O sistema foi desenvolvido com foco em segurança, integridade de dados e isolamento entre usuários (Multi-tenancy).

---

## 🚀 Funcionalidades Principais

### 🔒 Autenticação & Segurança
- **Registro e Login**: Sistema seguro com hash de senhas (BCrypt).
- **Token AES**: Autenticação baseada em tokens 100% criptografados no backend.
- **Multi-tenancy**: Cada usuário possui seu próprio isolamento de dados, garantido por um Interceptor de segurança.

### 🏦 Gestão de Contas & Saldo Dinâmico
- **Saldos em Tempo Real**: O saldo das contas não é estático; ele é calculado dinamicamente com base em todas as movimentações.
- **Categorização**: Organize suas receitas e despesas com categorias personalizadas.
- **Visão 360º**: Acompanhe o saldo disponível, o total investido e o valor reservado para metas em uma única consulta.

### 💸 Transações & Transferências
- **Filtros Avançados**: Busque transações por período, conta ou faixa de valores.
- **Double Entry (Partida Dobrada)**: Transferências entre contas garantem a consistência (saída em uma e entrada automática na outra).

### 📈 Investimentos & Metas (Goals)
- **Aportes Inteligentes**: Realize investimentos ou guarde dinheiro para metas com débito automático do saldo disponível.
- **Classificação de Ativos**: Organize investimentos por Renda Fixa ou Renda Variável.
- **Progresso de Metas**: Acompanhe o percentual de conclusão de seus objetivos financeiros.

---

## 🛠️ Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.4.6**
- **Spring Data JPA**
- **Oracle Database 21c (XE)**
- **jBCrypt** (Segurança)
- **Maven** (Gerenciamento de dependências)

---

## 💻 Como Executar o Projeto

Siga os passos abaixo para rodar a aplicação em sua máquina local:

### 1. Pré-requisitos
- **Java 21** instalado e configurado no PATH.
- **Maven** instalado.
- **Oracle Database** rodando localmente (ou acesso a um banco remoto).

### 2. Configuração do Banco de Dados
Abra o arquivo `src/main/resources/application.properties` e ajuste as credenciais de conexão:
```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521:XE
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```
*O sistema está configurado com `ddl-auto=update`, então as tabelas serão criadas automaticamente na primeira execução.*

Também, defina uma secret-key de 32 caracteres e um limite de tempo para o token expirar!
```secret-key
auth.secret-key=
auth.token-expiration-hours=24
```

### 3. Build e Execução
No terminal, na raiz do projeto, execute:
```bash
# Compilar o projeto
./mvnw clean compile

# Rodar a aplicação
./mvnw spring-boot:run
```
A API estará disponível em: `http://localhost:8080/api`

---

## 📑 Testando a API (Postman)

Incluímos um arquivo de coleção completo para facilitar seus testes:

1. Importe o arquivo `Fintech_Collection.json` (localizado na raiz do projeto) para o seu **Postman**.
2. No Postman, acesse a aba **Variables** da coleção e configure a `baseUrl` (geralmente `http://localhost:8080/api`).
3. **Fluxo de Teste Sugerido:**
   - Execute a rota `Auth > Register` para criar seu usuário.
   - Execute `Auth > Login` para obter o seu token.
   - Copie o token recebido e cole na variável `token` da coleção.
   - Agora todas as outras rotas (Contas, Transações, etc.) funcionarão automaticamente!

---

## 📐 Estrutura de Pacotes
- `controller`: Exposição dos endpoints REST.
- `service`: Regras de negócio e cálculos dinâmicos.
- `model`: Entidades JPA e mapeamento Oracle.
- `dto`: Objetos de transferência de dados para entrada e saída.
- `repository`: Interfaces de comunicação com o banco de dados.
- `config`: Configurações de segurança (CORS e Interceptors).

---
*Desenvolvido como parte do projeto Fintech - Fase 7.*
