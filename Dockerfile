# Estágio 1: Build da aplicação com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e o pom.xml
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# Baixa as dependências
RUN mvn dependency:go-offline

# Copia o código-fonte e compila a aplicação
COPY src ./src
RUN mvn package -DskipTests

# Estágio 2: Criação da imagem final otimizada
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o JAR da aplicação do estágio de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta em que a aplicação roda
EXPOSE 8080

# Define o comando para executar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
