Exercício Técnico - Evento

Requisitos

Antes de iniciar, certifique-se de ter os seguintes softwares instalados:

JDK 17

Maven

PostgreSQL

Configuração do Banco de Dados

CREATE DATABASE nome_do_banco;

Configurar o application.properties:
No arquivo src/main/resources/application.properties, ajuste as informações de conexão:

spring.datasource.url=jdbc:postgresql://localhost:5432/nome_do_banco
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha


Tecnologias Utilizadas

Spring Boot (Web, Data JPA)

PostgreSQL

Liquibase (Migração de banco de dados)

JSF com PrimeFaces e JoinFaces

Lombok
