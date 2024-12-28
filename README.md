# Sistema de Banking - JBank

Este projeto consiste na criação de um sistema de banking usando Spring Boot. O sistema permitirá a criação e gestão de carteiras bancárias, incluindo a realização de depósitos, transferências e consultas de extratos. Adicionalmente, o projeto inclui funcionalidades de **logging** para **auditoria de todas as transações e acesso ao sistema**.

## Funcionalidades do sistema

1. **Criar uma Carteira:** Permitir a criação de uma carteira bancária com informações como CPF, email e nome do titular.
2. **Encerrar uma Carteira:** Permitir o fechamento de uma carteira bancária existente, desde que o saldo esteja zerado.
3. **Depositar Dinheiro:** Realizar depósitos de dinheiro em uma carteira existente. Este serviço deve atualizar o saldo da carteira correspondente e registrar os dados na tabela de histórico de depósitos.
4. **Realizar Transferência:** Permitir a transferência de fundos de uma carteira para outra. Deve verificar a disponibilidade de saldo suficiente antes de completar a transação.
5. **Consultar Extrato:** Gerar e fornecer um extrato detalhado das transações realizadas em uma carteira, incluindo depósitos, transferências recebidas e enviadas, com data e hora.

## Pré-requisitos

Você precisa ter o CLI **docker** e **docker compose** (ou **docker-compose**) disponíveis no seu `PATH`. A versão mínima suportada do **Docker Compose é a 2.2.0**.

## Tecnologias Usadas
- Java 21
- Spring Boot
- Spring Data JPA
- Docker e Docker Compose
- Banco de dados MySQL (configurável via Docker Compose)

## Como executar o projeto

1. Clone o repositório:
```bash
git clone git@github.com:johnenderson/jbank.git
cd jbank
```

2. Inicie a aplicação:
```bash
./mvnw spring-boot:run
```

Configurações adicionais:

- Alterar porta: 
Para rodar em outra porta:
```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```
- Banco de Dados: 
O banco de dados será iniciado automaticamente com as configurações do **Docker Compose**. Confira o arquivo `docker-compose.yml` para detalhes.