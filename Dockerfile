# Build da aplicação com Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia os arquivos de configuração do Maven e o pom.xml
COPY .mvn/ .mvn/
COPY mvnw .
RUN chmod +x mvnw

COPY pom.xml .

# Baixa as dependências
RUN mvn dependency:go-offline

# Copia o código-fonte e compila a aplicação
COPY src ./src
RUN mvn package -DskipTests

# Criação da imagem
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copia o .jar da aplicação
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta em que a aplicação roda
EXPOSE 8080

# Define o comando para executar a aplicação
ENTRYPOINT ["java","-jar","app.jar"]
