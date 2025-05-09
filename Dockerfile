FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
# Étape de build avec Maven
FROM maven:3.9.6-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
# Copie des fichiers sources
COPY src ./src
# Construction en mode hors ligne pour accélérer le build
RUN mvn dependency:go-offline
RUN mvn clean package -DskipTests

# Étape d'exécution légère
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
# Copie uniquement le JAR construit
COPY --from=builder /app/target/*.jar app.jar
# Exposition du port (à adapter selon votre application)
EXPOSE 8000
ENTRYPOINT ["java", "-jar", "app.jar"]