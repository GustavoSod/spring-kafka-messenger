# Order System - Producer and Consumer with Apache Kafka

Este projeto é composto por dois microsserviços: `orderproducer` e `orderconsumer`. Juntos, eles demonstram uma arquitetura de **mensageria assíncrona** utilizando **Apache Kafka**, **Spring Boot** e **H2 Database** (banco em memória).

---

## 📦 Estrutura do Projeto

```
order-system/
├── orderproducer/   # Produz mensagens Kafka ao criar pedidos
└── orderconsumer/   # Consome mensagens Kafka e processa pedidos
```

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot
- Apache Kafka
- H2 Database
- Docker

---

## ⚙️ Como Rodar o Projeto

### 1. ✅ Pré-requisitos

- Java 17+
- Maven
- Docker

---

### 2. 🐳 Subindo o Kafka com Docker

```bash
# Baixar a imagem do Kafka
docker pull apache/kafka:4.0.0

# Subir o container
docker run -p 9092:9092 apache/kafka:4.0.0
```

> Certifique-se de que a porta 9092 está livre no seu sistema.

---

### 3. ▶️ Rodando os Microsserviços

#### Order Producer

```bash
cd orderproducer
./mvnw spring-boot:run
```

#### Order Consumer

Em outro terminal:

```bash
cd orderconsumer
./mvnw spring-boot:run
```

---

## 📬 Como Funciona

1. O `orderproducer` expõe uma API REST para criar pedidos.
2. Ao criar um pedido, ele publica uma mensagem no **tópico Kafka** `orders`.
3. O `orderconsumer` consome as mensagens desse tópico e processa os pedidos.
4. Os dados são armazenados temporariamente em um banco H2 em memória.

---

## 📮 Endpoint

**Criar pedido**
```http
POST /orders
Content-Type: application/json
```

---
